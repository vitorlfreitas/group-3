'use client';
import { useState } from 'react';

export default function HomePage() {
  const [tripDetails, setTripDetails] = useState('');
  const [userName, setUserName] = useState('Vitor');
  const [response, setResponse] = useState<any>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e: any) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const res = await fetch('http://localhost:8080/trip-plan', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ tripDetails, userName, generatePdf: true }),
      });

      if (!res.ok) throw new Error('Server Error');

      const data = await res.json();
      setResponse(data);
    } catch (err) {
      setError('Failed to fetch recommendations. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleDownload = () => {
    if (response?.pdfFileName) {
      const link = document.createElement('a');
      link.href = `http://localhost:8080/files/download/${response.pdfFileName}`;
      link.download = response.pdfFileName;
      link.target = '_blank';
      link.click();
    }
  };

  return (
    <main className="p-6 max-w-xl mx-auto">
      <h1 className="text-2xl font-bold mb-4">Trip Planner</h1>
      <form onSubmit={handleSubmit} className="space-y-4">
        <input
          type="text"
          placeholder="Describe your trip..."
          value={tripDetails}
          onChange={(e) => setTripDetails(e.target.value)}
          className="w-full p-2 border rounded"
        />
        <button
          type="submit"
          className="bg-blue-600 text-white px-4 py-2 rounded disabled:opacity-50"
          disabled={loading}
        >
          {loading ? 'Generating...' : 'Generate Checklist'}
        </button>
      </form>

      {error && <p className="text-red-600 mt-4">{error}</p>}

      {response && (
        <div className="mt-6">
          <h2 className="font-bold text-lg">Recommendations:</h2>
          <pre className="whitespace-pre-wrap bg-gray-100 p-4 mt-2 rounded">
            {response.recommendations}
          </pre>
          {response.pdfFileName && (
            <button
              onClick={handleDownload}
              className="mt-4 text-blue-600 underline"
            >
              📄 Download your checklist PDF
            </button>
          )}
        </div>
      )}
    </main>
  );
}
