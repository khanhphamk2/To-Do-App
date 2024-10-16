import { useRef, useEffect } from "react";
import PropTypes from "prop-types";
import RoundCheckbox from "../../controls/checkbox/RoundCheckbox";
import StarCheckbox from "../../controls/checkbox/StarCheckbox";
import "./style/Task.css";
import { timeDiffFromNow } from "../../../utils/formatting";

export default function Task({ id, title, description, date, time, onEdit }) {
  const taskRef = useRef(null);

  const taskAlert = () => {
    return timeDiffFromNow(time).diffSeconds < 0;
  };

  useEffect(() => {
    const taskElement = taskRef.current;
    taskElement.classList.add("task-enter");
    setTimeout(() => {
      taskElement.classList.add("task-enter-active");
    }, 10);
  }, []);

  return (
    <div
      ref={taskRef}
      className="task-hover relative flex items-center gap-4 bg-[#FFFFFF] px-4 py-3 rounded-xl shadow-lg min-h-[72px] justify-between transition-transform transform hover:scale-105 group"
    >
      <div className="flex items-center gap-4">
        <RoundCheckbox />
        <div className="flex flex-col justify-center">
          <div className="flex gap-2">
            <p className="text-black text-base font-medium leading-normal line-clamp-1">
              {title}
            </p>
          </div>
          <p className="text-neutral-500 text-sm font-normal leading-normal line-clamp-2">
            {description}
          </p>
        </div>
      </div>
      <div className="flex items-center gap-2">
        <div className="shrink-0">
          <p className="text-neutral-500 text-sm font-normal leading-normal">
            {date}
          </p>
        </div>
        <div className="shrink-0">
          <p className="text-neutral-500 text-sm font-normal leading-normal">
            {time}
          </p>
        </div>
      </div>
      <div className="flex items-center gap-4">
        <div className="shrink-0">
          <StarCheckbox />
        </div>
        <div className="cursor-pointer" onClick={() => onEdit(id)}>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            strokeWidth={1.5}
            stroke="currentColor"
            className="size-6"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              d="M12 6.75a.75.75 0 1 1 0-1.5.75.75 0 0 1 0 1.5ZM12 12.75a.75.75 0 1 1 0-1.5.75.75 0 0 1 0 1.5ZM12 18.75a.75.75 0 1 1 0-1.5.75.75 0 0 1 0 1.5Z"
            />
          </svg>
        </div>
      </div>
      {taskAlert() && (
        <div className="task-warning relative">
          <span className="text-xs font-bold">!</span>
          <div className="tooltip absolute top-[-30px] left-[-10px] bg-black text-white text-xs rounded py-1 px-2 opacity-0 transition-opacity duration-300">
            Task overdue
          </div>
        </div>
      )}
    </div>
  );
}

Task.propTypes = {
  id: PropTypes.number.isRequired,
  title: PropTypes.string.isRequired,
  description: PropTypes.string.isRequired,
  date: PropTypes.string.isRequired,
  time: PropTypes.string.isRequired,
  onEdit: PropTypes.func.isRequired,
};
