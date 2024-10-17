import React from 'react';

export default function RegisterForm() {
  return (
    <div>
      <div className="space-y-6 transition-all duration-500">
        <label className="block">
          <span className="text-base font-medium text-[#111418]">Email</span>
          <input
            type="email"
            placeholder="you@example.com"
            className="w-full mt-2 h-12 px-4 py-2 border border-[#dce0e5] rounded-xl text-[#111418] focus:outline-none focus:border-[#1980e6]"
          />
        </label>
        <label className="block">
          <span className="text-base font-medium text-[#111418]">Password</span>
          <input
            type="password"
            placeholder="Enter a password"
            className="w-full mt-2 h-12 px-4 py-2 border border-[#dce0e5] rounded-xl text-[#111418] focus:outline-none focus:border-[#1980e6]"
          />
        </label>

        <label className="block transition-all duration-500">
          <span className="text-base font-medium text-[#111418]">
            Confirm Password
          </span>
          <input
            type="password"
            placeholder="Confirm your password"
            className="w-full mt-2 h-12 px-4 py-2 border border-[#dce0e5] rounded-xl text-[#111418] focus:outline-none focus:border-[#1980e6]"
          />
        </label>
      </div>

      <div className="mt-8">
        <button className="w-full h-12 bg-[#1980e6] text-white font-bold rounded-xl transition-colors duration-300">
          Register
        </button>
      </div>
    </div>
  );
}
