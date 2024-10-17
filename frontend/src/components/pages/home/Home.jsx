import React from "react";
import Header from "../../templates/header/Header";
import TodoList from "../../templates/todo-list/TodoList";
// import { useContext } from "react";
// import { AuthContext } from "../../../context/authContext";

export default function Home() {
  // const { token, login } = useContext(AuthContext);
  return (
    <div>
      <Header />
      <div className="px-40 flex flex-1 justify-center py-5">
        <TodoList />
      </div>
    </div>
  );
}
