import { useState, useEffect, useCallback } from "react";
import {
  PopoverGroup,
  Popover,
  PopoverButton,
  PopoverPanel,
} from "@headlessui/react";
import {
  ChevronDownIcon,
  UserCircleIcon,
  StarIcon,
  ArrowRightEndOnRectangleIcon,
  MoonIcon,
  CheckCircleIcon,
} from "@heroicons/react/20/solid";
import { HomeIcon, SunIcon } from "@heroicons/react/24/outline";
import { useAuth } from "../../../context/AuthProvider";

export default function Header() {
  const [username, setUsername] = useState(null);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const auth = useAuth();

  const handleLogin = useCallback(() => {
    if (auth.isAuthenticated()) {
      const user = auth.getUser();
      setUsername(user.username);
      setIsLoggedIn(true);
    }
  }, [auth]);

  useEffect(() => {
    handleLogin();
  }, [handleLogin]);

  const handleLogout = () => {
    auth.logout();
    setUsername(null);
    setIsLoggedIn(false);
    window.location.href = "/";
  };

  const products = [
    {
      name: "Dark mode",
      href: "#",
      icon: MoonIcon,
      onClick: (e) => {
        e.preventDefault();
        document.documentElement.classList.remove("dark"); // Xóa class dark khỏi thẻ <html>
        console.log("clicked");
      },
    },
    {
      name: "Log out",
      href: "",
      icon: ArrowRightEndOnRectangleIcon,
      onClick: (e) => {
        e.preventDefault();
        handleLogout();
      },
    },
  ];

  return (
    <header className="bg-white">
      <nav
        aria-label="Global"
        className="mx-auto flex max-w-7xl items-center justify-between p-6 lg:px-8"
      >
        <div className="flex lg:flex-1">
          <button href="#" className="-m-1.5 p-1.5 flex items-center gap-2">
            <div className="size-7">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                className="w-full h-full"
                viewBox="0 0 48 48"
              >
                <path
                  fill="#0d47a1"
                  d="M21.25,40.454l6.13-6.142c0.694-0.696,0.694-1.823,0-2.519L11.165,15.547 c-0.694-0.696-1.82-0.696-2.514,0l-6.13,6.142c-0.694,0.696-0.694,1.823,0,2.519l16.215,16.246 C19.43,41.15,20.555,41.15,21.25,40.454z"
                ></path>
                <path
                  fill="#29b6f6"
                  d="M18.788,40.498l-6.223-6.235c-0.669-0.67-0.669-1.756,0-2.426L35.854,8.502 c0.669-0.67,1.753-0.67,2.421,0l6.223,6.235c0.669,0.67,0.669,1.756,0,2.426L21.209,40.498 C20.541,41.167,19.457,41.167,18.788,40.498z"
                ></path>
              </svg>
            </div>
            <h2 className="text-black text-lg font-bold leading-tight tracking-[-0.015em]">
              To Do
            </h2>
          </button>
        </div>
        <PopoverGroup className="hidden lg:flex lg:gap-x-12 ">
          <a
            href="/"
            className="flex items-center gap-1 text-sm font-semibold leading-6 text-gray-900 hover:text-blue-700"
          >
            <HomeIcon className="h-5 w-5 text-gray-900" />
            Tasks
          </a>
          <a
            // href="/My-Day"
            href="#"
            className="flex items-center gap-1 text-sm font-semibold leading-6 text-gray-900"
          >
            <SunIcon className="h-5 w-5 text-gray-900" />
            My Day
          </a>
          <a
            href="#"
            className="flex items-center gap-1 text-sm font-semibold leading-6 text-gray-900"
          >
            <StarIcon className="h-5 w-5 text-gray-900" />
            Important
          </a>
          <a
            href="#"
            className="flex items-center gap-1 text-sm font-semibold leading-6 text-gray-900"
          >
            <CheckCircleIcon className="h-5 w-5 text-gray-900" />
            Completed
          </a>
        </PopoverGroup>
        <div className="hidden lg:flex lg:flex-1 lg:justify-end lg:items-center gap-6">
          {isLoggedIn ? (
            <Popover className="relative">
              <PopoverButton className="flex items-center gap-x-1 text-sm font-semibold leading-6 text-gray-900">
                <UserCircleIcon class="h-7 w-7 text-gray-500 mr-1" /> {username}
                <ChevronDownIcon
                  aria-hidden="true"
                  className="h-5 w-5 flex-none text-gray-400"
                />
              </PopoverButton>
              <PopoverPanel
                transition
                className="absolute -left-8 top-full z-10 mt-3 w-screen max-w-xs overflow-hidden rounded-3xl bg-white shadow-lg ring-1 ring-gray-900/5 transition data-[closed]:translate-y-1 data-[closed]:opacity-0 data-[enter]:duration-200 data-[leave]:duration-150 data-[enter]:ease-out data-[leave]:ease-in"
              >
                <div className="p-2">
                  {" "}
                  {products.map((item) => (
                    <div
                      key={item.name}
                      className="group relative flex items-center gap-x-4 rounded-lg p-2 text-sm leading-6 hover:bg-gray-50"
                    >
                      <div className="flex h-10 w-10 flex-none items-center justify-center rounded-lg bg-gray-50 group-hover:bg-white">
                        {" "}
                        <item.icon
                          aria-hidden="true"
                          className={`h-5 w-5 ${
                            item.name === "Log out"
                              ? "text-red-600"
                              : "text-gray-600 group-hover:text-indigo-600"
                          }`}
                        />
                      </div>
                      <div className="flex-auto">
                        <a
                          href={item.href}
                          className={`block font-semibold ${
                            item.name === "Log out"
                              ? "text-red-600"
                              : "text-gray-900"
                          }`}
                          onClick={item.onClick}
                        >
                          {item.name}
                          <span className="absolute inset-0" />
                        </a>
                      </div>
                    </div>
                  ))}
                </div>
              </PopoverPanel>
            </Popover>
          ) : (
            <a
              href="/login"
              className="text-sm font-semibold leading-6 text-gray-900"
            >
              Log in <span aria-hidden="true">&rarr;</span>
            </a>
          )}
        </div>
      </nav>
    </header>
  );
}
