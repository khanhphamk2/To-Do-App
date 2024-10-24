import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../../../context/AuthProvider";
import { login } from "../../../../api/auth.api";

export default function LoginForm() {
  const [identity, setIdentity] = useState("madison66@info.me");
  const [password, setPassword] = useState("admin@123");
  const auth = useAuth();
  const navigate = useNavigate();

  const loginData = {
    identity: identity,
    password: password,
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    const response = await login(loginData);
    auth.login(response);
    navigate("/");
  };

  return (
    <div>
      <form onSubmit={handleLogin}>
        <div className="space-y-6 transition-all duration-500">
          <label className="block">
            <span className="text-base font-medium text-[#111418]">
              Email or Username
            </span>
            <input
              type="email"
              placeholder="you@example.com"
              className="w-full mt-2 h-12 px-4 py-2 border border-[#dce0e5] rounded-xl text-[#111418] focus:outline-none focus:border-[#1980e6]"
              value={identity} // setting the default value for email/username
              onChange={(e) => setIdentity(e.target.value)}
              required
            />
          </label>
          <label className="block">
            <span className="text-base font-medium text-[#111418]">
              Password
            </span>
            <input
              type="password"
              placeholder="Enter a password"
              className="w-full mt-2 h-12 px-4 py-2 border border-[#dce0e5] rounded-xl text-[#111418] focus:outline-none focus:border-[#1980e6]"
              value={password} // setting the default value for password
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </label>
        </div>

        <div className="mt-8">
          <button
            type="submit"
            className="w-full h-12 bg-[#1980e6] text-white font-bold rounded-xl transition-colors duration-300"
          >
            Login
          </button>
        </div>
      </form>

      <div className="mt-4 text-center text-sm text-[#637588]">
        Or login with
      </div>
      <div className="mt-4 flex justify-center gap-3">
        <button className="flex-1 h-12 bg-[#f0f2f4] text-[#111418] font-bold rounded-xl flex items-center justify-center gap-2">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 48 48"
            className="size-7"
          >
            <path
              fill="#FFC107"
              d="M43.611,20.083H42V20H24v8h11.303c-1.649,4.657-6.08,8-11.303,8c-6.627,0-12-5.373-12-12c0-6.627,5.373-12,12-12c3.059,0,5.842,1.154,7.961,3.039l5.657-5.657C34.046,6.053,29.268,4,24,4C12.955,4,4,12.955,4,24c0,11.045,8.955,20,20,20c11.045,0,20-8.955,20-20C44,22.659,43.862,21.35,43.611,20.083z"
            />
            <path
              fill="#FF3D00"
              d="M6.306,14.691l6.571,4.819C14.655,15.108,18.961,12,24,12c3.059,0,5.842,1.154,7.961,3.039l5.657-5.657C34.046,6.053,29.268,4,24,4C16.318,4,9.656,8.337,6.306,14.691z"
            />
            <path
              fill="#4CAF50"
              d="M24,44c5.166,0,9.86-1.977,13.409-5.192l-6.19-5.238C29.211,35.091,26.715,36,24,36c-5.202,0-9.619-3.317-11.283-7.946l-6.522,5.025C9.505,39.556,16.227,44,24,44z"
            />
            <path
              fill="#1976D2"
              d="M43.611,20.083H42V20H24v8h11.303c-0.792,2.237-2.231,4.166-4.087,5.571c0.001-0.001,0.002-0.001,0.003-0.002l6.19,5.238C36.971,39.205,44,34,44,24C44,22.659,43.862,21.35,43.611,20.083z"
            />
          </svg>
          Google
        </button>
        <button className="flex-1 h-12 bg-[#f0f2f4] text-[#111418] font-bold rounded-xl flex items-center justify-center gap-2">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            x="0px"
            y="0px"
            viewBox="0 0 48 48"
            className="size-7"
          >
            <path
              fill="#1877F2"
              d="M24 4A20 20 0 1 0 24 44A20 20 0 1 0 24 4Z"
            ></path>
            <path
              fill="#fff"
              d="M29.368,24H26v12h-5V24h-3v-4h3v-2.41c0.002-3.508,1.459-5.59,5.592-5.59H30v4h-2.287 C26.104,16,26,16.6,26,17.723V20h4L29.368,24z"
            ></path>
          </svg>
          Facebook
        </button>
      </div>

      <div className="mt-4 text-center text-sm text-[#886367] underline cursor-pointer">
        Forgot your password?
      </div>
    </div>
  );
}
