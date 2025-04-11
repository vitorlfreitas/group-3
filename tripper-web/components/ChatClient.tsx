"use client";
import { Session } from "next-auth";
import { useEffect, useState, useRef } from "react";
import SockJS from "sockjs-client";
import { Client, IMessage } from "@stomp/stompjs";
import { User } from "lucide-react";
import ReactMarkdown from "react-markdown";
import { Trash2 } from "lucide-react";

let stompClient: Client | null = null;

type Conversation = {
    id: number;
    title: string | null;
    startedAt: string;
};

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
    const [userConversations, setUserConversations] = useState<Conversation[]>(
        []
    );

    const [editingId, setEditingId] = useState<number | null>(null);
    const [editTitle, setEditTitle] = useState("");

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
                body: JSON.stringify({ userId: user.email }),
            });

            let data;
            try {
                data = await res.json();
                console.log("✅ Parsed response:", data);
            } catch (err) {
                console.error("❌ Failed to parse JSON:", err);
            }

            setConversationId(data.conversationId);

            const historyRes = await fetch(
                `http://localhost:8080/chat/history?userId=${user.email}&conversationId=${data.conversationId}`
            );

            if (historyRes.ok) {
                let history;
                try {
                    history = await historyRes.json();
                    console.log("✅ Fetched history:", history);
                } catch (err) {
                    console.error("❌ Failed to parse history JSON:", err);
                }
                setMessages(history);
            }

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

    useEffect(() => {
        const fetchConversations = async () => {
            const res = await fetch(
                `http://localhost:8080/chat/user/${user.email}`
            );
            let data;
            try {
                data = await res.json();
                console.log("✅ Fetched conversations:", data);
            } catch (err) {
                console.error("❌ Failed to parse conversations JSON:", err);
            }
            setUserConversations(data);
            console.log("Fetched conversations:", data);
        };

        if (user?.email) {
            fetchConversations();
        }
    }, [user]);

    const loadConversation = async (conversationId: number) => {
        const res = await fetch(
            `http://localhost:8080/chat/${conversationId}/messages`
        );
        let history;
        try {
            history = await res.json();
            console.log("✅ Fetched conversation history:", history);
        } catch (err) {
            console.error("❌ Failed to parse conversation history JSON:", err);
        }
        setConversationId(conversationId);
        setMessages(history);
    };

    const updateTitle = async (id: number) => {
        await fetch(`http://localhost:8080/chat/${id}/title`, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ title: editTitle }),
        });

        // Refresh list
        const res = await fetch(
            `http://localhost:8080/chat/user/${user.email}`
        );
        const data = await res.json();
        setUserConversations(data);
        setEditingId(null);
    };

    const handleDelete = async (id: number) => {
        const confirmed = window.confirm("Are you sure you want to delete this conversation?");
        if (!confirmed) return;
      
        await fetch(`http://localhost:8080/chat/${id}`, {
          method: "DELETE",
        });
      
        // Refresh the conversation list
        const res = await fetch(`http://localhost:8080/chat/user/${user.email}`);
        const data = await res.json();
        setUserConversations(data);
      
        if (conversationId === id) {
          setConversationId(null);
          setMessages([]);
        }
      };
      

    return (
        <main className="h-screen bg-gray-200 flex">
            {/* Sidebar */}
            <aside className="w-64 border-r bg-white p-4 overflow-y-auto">
                <h2 className="text-lg font-semibold mb-4">
                    Your Conversations
                </h2>
                <ul className="space-y-2">
                    {userConversations.map((conv) => (
                        <li key={conv.id}>
                            <div className="flex items-center justify-between group p-2 rounded hover:bg-gray-100 cursor-pointer">
                                {editingId === conv.id ? (
                                    <input
                                        className="w-full p-1 border rounded text-sm"
                                        value={editTitle}
                                        onChange={(e) =>
                                            setEditTitle(e.target.value)
                                        }
                                        onBlur={() => updateTitle(conv.id)}
                                        onKeyDown={(e) =>
                                            e.key === "Enter" &&
                                            updateTitle(conv.id)
                                        }
                                        autoFocus
                                    />
                                ) : (
                                    <div
                                        onDoubleClick={() => {
                                            setEditingId(conv.id);
                                            setEditTitle(conv.title ?? "");
                                        }}
                                        className="text-left w-full"
                                        onClick={() => loadConversation(conv.id)}
                                    >
                                        {conv.title && conv.title.trim() !== ""
                                            ? conv.title
                                            : new Date(
                                                  conv.startedAt
                                              ).toLocaleString()}
                                    </div>
                                )}
                                {/* Trash Icon */}
                                <button
                                    onClick={() => handleDelete(conv.id)}
                                    className="opacity-0 group-hover:opacity-100 text-red-500 hover:text-red-700 transition cursor-pointer"
                                >
                                    <Trash2 size={16} />
                                </button>
                            </div>
                        </li>
                    ))}
                </ul>
            </aside>

            {/* Chat Window */}
            <section className="flex-1 flex flex-col items-center justify-center px-4 py-8">
                <div className="w-full max-w-2xl flex flex-col bg-white shadow-md rounded-2xl overflow-hidden h-full">
                    <div className="flex-1 overflow-y-auto px-4 py-6 space-y-4">
                        {messages.length > 0 ? (
                            messages.map((msg, index) => {
                                const isUser = msg.sender === "user";
                                return (
                                    <div
                                        key={index}
                                        className={`flex items-start gap-2 ${
                                            isUser
                                                ? "justify-end"
                                                : "justify-start"
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
                            })
                        ) : (
                            <p className="text-gray-500 text-center mt-12">
                                {conversationId
                                    ? "No messages yet."
                                    : "Select a conversation to view messages."}
                            </p>
                        )}

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

                    {/* Input */}
                    {conversationId && (
                        <div className="p-4 flex gap-2">
                            <input
                                type="text"
                                className="flex-1 border border-gray-300 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                                placeholder="Ask Tripper about your trip..."
                                value={input}
                                onChange={(e) => setInput(e.target.value)}
                                onKeyDown={(e) =>
                                    e.key === "Enter" && sendMessage()
                                }
                            />
                            <button
                                onClick={sendMessage}
                                className="bg-blue-600 text-white px-5 py-2 rounded-lg hover:bg-blue-700 transition hover:cursor-pointer"
                                disabled={!connected}
                            >
                                Send
                            </button>
                        </div>
                    )}
                </div>
            </section>
        </main>
    );
}
