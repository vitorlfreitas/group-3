import React from 'react';

const ChatPage = () => {
    const messages = [
        { id: 1, sender: 'user', text: 'Hello!' },
        { id: 2, sender: 'bot', text: 'Hi there! How can I help you?' },
        { id: 3, sender: 'user', text: 'I need some assistance.' },
    ];

    return (
        <div className="p-4">
            {messages.map((msg) => (
                <div key={msg.id} className={msg.sender === 'user' ? 'text-right' : 'text-left'}>
                    <div
                        className={`inline-block px-3 py-2 rounded-lg ${
                            msg.sender === 'user' ? 'bg-blue-500 text-white' : 'bg-gray-200 text-gray-900'
                        }`}
                    >
                        {msg.text}
                    </div>
                </div>
            ))}
        </div>
    );
};

export default ChatPage;