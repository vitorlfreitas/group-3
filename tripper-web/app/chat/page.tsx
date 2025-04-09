"use client";
import { useEffect, useState, useRef } from "react";
import SockJS from "sockjs-client";
import { Client, IMessage } from "@stomp/stompjs";
import { Bot, User } from "lucide-react";

let stompClient: Client | null = null;

type Message = {
    sender: "user" | "assistant";
    content: string;
    timestamp?: string;
};

export default function ChatPage() {
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
                body: JSON.stringify({ userId: "vitor@example.com" }),
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
        if (!input.trim() || conversationId === null || !stompClient?.connected) return;
      
        const payload = {
          userId: 'vitor@example.com',
          content: input,
          conversationId,
        };
      
        // Optimistically add user message to chat
        const newMessage: Message = {
          sender: 'user',
          content: input,
          timestamp: new Date().toISOString(),
        };
        setMessages((prev) => [...prev, newMessage]);
      
        // Clear input + set typing
        setInput('');
        setIsTyping(true);
      
        // Send to backend
        stompClient.publish({
          destination: '/app/chat.send',
          body: JSON.stringify(payload),
        });
      };
      

    useEffect(() => {
        bottomRef.current?.scrollIntoView({ behavior: "smooth" });
    }, [messages]);

    return (
        <main className="max-w-2xl mx-auto px-4 py-6">
            <h1 className="text-2xl font-bold mb-6 text-center">
                🌍 Tripper Chatbot
            </h1>

            <div className="bg-white border rounded-lg p-4 h-[450px] overflow-y-auto shadow flex flex-col space-y-3">
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
                                <img src="/tripper.png" alt="Tripper" className="w-8 h-8 rounded-full" />
                            )}
                            <div
                                className={`max-w-[75%] px-4 py-2 rounded-lg shadow-sm ${
                                    isUser
                                        ? "bg-blue-600 text-white self-end"
                                        : "bg-gray-100 text-gray-800"
                                }`}
                            >
                                <p className="whitespace-pre-wrap text-sm">
                                    {msg.content}
                                </p>
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
                                    <User className="w-6 h-6 text-gray-800" />
                                </div>
                            )}
                        </div>
                    );
                })}

                {isTyping && (
                    <div className="flex items-center gap-2">
                        <img src="/tripper.png" alt="Tripper" className="w-8 h-8 rounded-full" />
                        <div className="bg-gray-200 px-4 py-2 rounded-lg text-sm animate-pulse text-gray-800">
                            Tripper is typing
                            <span className="animate-bounce">...</span>
                        </div>
                    </div>
                )}
            </div>

            <div className="mt-4 flex gap-2">
                <input
                    type="text"
                    className="flex-1 border p-2 rounded"
                    placeholder="Type your message..."
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                    onKeyDown={(e) => e.key === "Enter" && sendMessage()}
                />
                <button
                    onClick={sendMessage}
                    className="bg-blue-600 text-white px-4 py-2 rounded"
                    disabled={!connected}
                >
                    Send
                </button>
            </div>
        </main>
    );
}
