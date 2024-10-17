import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import LoginForm from "./form/LoginForm";
import RegisterForm from "./form/RegisterForm";

function Authentication({ mode }) {
  const [authMode, setAuthMode] = useState(mode || "login");
  const navigate = useNavigate();

  useEffect(() => {
    navigate(`/${authMode}`);
  }, [authMode, navigate]);

  return (
    <div className="flex justify-center py-10 px-6 bg-white h-screen items-center">
      <div className="flex flex-col w-full max-w-[690px] bg-white p-6 px-8 pb-8 rounded-2xl shadow-[0_10px_30px_rgba(0,0,0,0.2)] transition-shadow duration-500 ease-in-out h-fit">
        <h1 className="text-center text-2xl font-bold text-[#111418] py-4 mb-2">
          Welcome to To Do App
        </h1>

        <div className="relative flex justify-center pb-4 mb-4">
          <div
            className={`absolute bottom-0 left-0 w-1/2 h-[2px] bg-[#1980e6] transition-transform duration-300 ${
              authMode === "login" ? "translate-x-0" : "translate-x-full"
            }`}
          />

          <button
            className={`flex-1 text-center pb-3 ${
              authMode === "login" ? "text-[#111418]" : "text-[#637588]"
            }`}
            onClick={() => setAuthMode("login")}
          >
            <p className="text-sm font-bold">Login</p>
          </button>

          <button
            className={`flex-1 text-center pb-3 ${
              authMode === "register" ? "text-[#111418]" : "text-[#637588]"
            }`}
            onClick={() => setAuthMode("register")}
          >
            <p className="text-sm font-bold">Register</p>
          </button>
        </div>

        {authMode === "login" ? <LoginForm /> : <RegisterForm />}
      </div>
    </div>
  );
}

export default Authentication;
