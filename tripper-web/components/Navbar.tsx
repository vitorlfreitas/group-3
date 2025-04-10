"use client";
import { AuthButton } from "./AuthButton";

export function Navbar() {
  return (
    <header className="h-2/12 flex justify-between items-center py-4 px-8 bg-gray-200">
      <h1 className="text-2xl font-bold text-gray-800">Tripper</h1>
      <AuthButton />
    </header>
  );
}
