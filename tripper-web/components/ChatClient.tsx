"use client";
import { Session } from "next-auth";
import { useEffect, useState, useRef } from "react";
import SockJS from "sockjs-client";
import { Client, IMessage } from "@stomp/stompjs";
import { User } from "lucide-react";
import ReactMarkdown from "react-markdown";

let stompClient: Client | null = null;

type Message = {
    sender: "user" | "assistant";
    content: string;
    timestamp?: string;
};

export default function ChatClient({ user }: { user: Session["user"] }) {
    const [connected, setConnected] = useState(false);
    const [conversationId, setConversationId] = useState<number | null>(null);
    const [messages, setMessages] = useState<Message[]>([]);
    const [input, setInput] = useState("");
    const bottomRef = useRef<HTMLDivElement>(null);
    const [isTyping, setIsTyping] = useState(false);

    useEffect(() => {
        const socket = new SockJS("http://localhost:8080/ws-chat");
        const client = new Client({
            webSocketFactory: () => socket as WebSocket,
            debug: (str) => console.log("[STOMP]", str),
            reconnectDelay: 5000,
        });

        stompClient = client;

        client.onConnect = async () => {
            setConnected(true);

            const res = await fetch("http://localhost:8080/chat/start", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ userId: user.id }),
            });

            const data = await res.json();
            setConversationId(data.conversationId);

            client.subscribe(
                `/topic/chat/${data.conversationId}`,
                (msg: IMessage) => {
                    const body = JSON.parse(msg.body);
                    setMessages(body);
                    setIsTyping(false);
                }
            );
        };

        client.activate();

        return () => {
            client.deactivate();
        };
    }, []);

    const sendMessage = () => {
        if (!input.trim() || conversationId === null || !stompClient?.connected)
            return;

        const payload = {
            userId: user.id,
            content: input,
            conversationId,
        };

        // Optimistically add user message to chat
        const newMessage: Message = {
            sender: "user",
            content: input,
            timestamp: new Date().toISOString(),
        };
        setMessages((prev) => [...prev, newMessage]);

        // Clear input + set typing
        setInput("");
        setIsTyping(true);

        // Send to backend
        stompClient.publish({
            destination: "/app/chat.send",
            body: JSON.stringify(payload),
        });
    };

    useEffect(() => {
        bottomRef.current?.scrollIntoView({ behavior: "smooth" });
    }, [messages]);

    return (
        <main className="h-10/12 bg-gray-200 flex flex-col items-center justify-center px-4 py-8">
            <div className="w-full max-w-2xl flex flex-col bg-white shadow-md rounded-2xl overflow-hidden h-full">
                <div className="flex-1 overflow-y-auto px-4 py-6 space-y-4">
                    {messages.map((msg, index) => {
                        const isUser = msg.sender === "user";
                        return (
                            <div
                                key={index}
                                className={`flex items-start gap-2 ${
                                    isUser ? "justify-end" : "justify-start"
                                }`}
                            >
                                {!isUser && (
                                    <img
                                        src="/tripper.png"
                                        alt="Tripper"
                                        className="w-8 h-8 rounded-full"
                                    />
                                )}
                                <div
                                    className={`max-w-[75%] px-4 py-2 rounded-2xl text-sm ${
                                        isUser
                                            ? "bg-blue-600 text-white rounded-br-none"
                                            : "bg-gray-200 text-gray-900 rounded-bl-none"
                                    }`}
                                >
                                    <div className="prose prose-sm text-sm">
                                        <ReactMarkdown>
                                            {msg.content}
                                        </ReactMarkdown>
                                    </div>
                                    {msg.timestamp && (
                                        <p className="text-xs text-right opacity-60 mt-1">
                                            {new Date(
                                                msg.timestamp
                                            ).toLocaleTimeString([], {
                                                hour: "2-digit",
                                                minute: "2-digit",
                                            })}
                                        </p>
                                    )}
                                </div>
                                {isUser && (
                                    <div className="p-2 bg-gray-300 rounded-full">
                                        <User className="w-5 h-5 text-gray-800" />
                                    </div>
                                )}
                            </div>
                        );
                    })}

                    {isTyping && (
                        <div className="flex items-center gap-2">
                            <img
                                src="/tripper.png"
                                alt="Tripper"
                                className="w-8 h-8 rounded-full"
                            />
                            <div className="bg-gray-200 px-4 py-2 rounded-xl text-sm animate-pulse text-gray-800">
                                Tripper is typing
                                <span className="animate-bounce">...</span>
                            </div>
                        </div>
                    )}

                    <div ref={bottomRef} />
                </div>

                <div className="p-4 flex gap-2">
                    <input
                        type="text"
                        className="flex-1 border border-gray-300 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                        placeholder="Ask Tripper about your trip..."
                        value={input}
                        onChange={(e) => setInput(e.target.value)}
                        onKeyDown={(e) => e.key === "Enter" && sendMessage()}
                    />
                    <button
                        onClick={sendMessage}
                        className="bg-blue-600 text-white px-5 py-2 rounded-lg hover:bg-blue-700 transition hover:cursor-pointer"
                        disabled={!connected}
                    >
                        Send
                    </button>
                </div>
            </div>
        </main>
    );
}
