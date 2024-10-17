import { useState, useEffect } from "react";
import Task from "../task/Task";
import TaskEdit from "../task/TaskEdit";
import { TASKS } from "../../../mock";
import { DragDropContext, Draggable, Droppable } from "react-beautiful-dnd";

export default function TodoList() {
  const [tasks, setTasks] = useState([]);
  const [isAdding, setIsAdding] = useState(false);
  const [newTask, setNewTask] = useState(null);

  useEffect(() => {
    setTasks(TASKS);
  }, []);

  const showEdit = (id) => {
    checkOtherShowEdit();
    const task = tasks.find((task) => task.id === id);
    task.editMode = true;
    setTasks([...tasks]);
  };

  const checkOtherShowEdit = () => {
    const editingTasks = tasks.filter((task) => task.editMode);
    if (editingTasks.length > 0) {
      editingTasks.forEach((task) => {
        task.editMode = false;
      });
      setTasks([...tasks]);
    }
    setIsAdding(false);
  };

  const closeEdit = (id) => {
    const task = tasks.find((task) => task.id === id);
    task.editMode = false;
    setTasks([...tasks]);
  };

  const addTask = () => {
    checkOtherShowEdit();
    setIsAdding(true);
    setNewTask({
      id: tasks.length + 1,
      title: "",
      description: "",
      date: new Date(),
      time: "00:00",
      editMode: true,
    });
  };

  const saveTask = (taskData) => {
    const taskToAdd = { ...newTask, ...taskData, editMode: false };
    setTasks((prevTasks) => [...prevTasks, taskToAdd]);
    setIsAdding(false);
    setNewTask(null);
  };

  const cancelAddTask = () => {
    setIsAdding(false);
    setNewTask(null);
  };

  const onDragEnd = (result) => {
    if (!result.destination) return;

    const updatedTasks = [...tasks];
    const [removed] = updatedTasks.splice(result.source.index, 1);
    updatedTasks.splice(result.destination.index, 0, removed);

    setTasks(updatedTasks);
  };

  return (
    <div className="layout-content-container flex flex-col max-w-[960px] flex-1">
      <div className="mt-4">
        <p className="text-black tracking-light text-[32px] font-bold leading-tight min-w-72 mb-4">
          To Do List
        </p>
        <div className="task-container">
          {!isAdding && (
            <div className="flex flex-col justify-start gap-4">
              <div className="flex gap-4 justify-end">
                <button
                  className="flex items-center bg-blue-500 text-white py-2 px-4 rounded-xl transition-all duration-300 hover:bg-blue-600 transform shadow-lg mb-4"
                  onClick={addTask}
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    strokeWidth="1.5"
                    stroke="currentColor"
                    className="size-4"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      d="M12 4.5v15m7.5-7.5h-15"
                    />
                  </svg>
                  <span className="ml-1">Add Task</span>
                </button>
              </div>
            </div>
          )}
          <DragDropContext onDragEnd={onDragEnd}>
            <Droppable droppableId="ROOT" type="group">
              {(provided) => (
                <div
                  {...provided.droppableProps}
                  ref={provided.innerRef}
                  className="flex flex-col gap-5 min-h-full"
                >
                  {tasks.map((task, index) => (
                    <Draggable
                      key={task.id}
                      draggableId={task.id.toString()}
                      index={index}
                    >
                      {(provided) => (
                        <div
                          {...provided.dragHandleProps}
                          {...provided.draggableProps}
                          ref={provided.innerRef}
                        >
                          {task.editMode ? (
                            <TaskEdit
                              id={task.id}
                              title={task.title}
                              description={task.description}
                              date={task.date}
                              time={task.time}
                              onCancel={closeEdit}
                              onSave={saveTask}
                            />
                          ) : (
                            <Task
                              id={task.id}
                              title={task.title}
                              description={task.description}
                              date={task.date}
                              time={task.time}
                              onEdit={showEdit}
                            />
                          )}
                        </div>
                      )}
                    </Draggable>
                  ))}
                  {provided.placeholder}
                </div>
              )}
            </Droppable>
          </DragDropContext>
          {isAdding && (
            <TaskEdit
              id={newTask.id}
              title={newTask.title}
              description={newTask.description}
              date={newTask.date}
              time={newTask.time}
              priority={tasks.length + 1}
              onSave={saveTask}
              onCancel={cancelAddTask}
              addMode
            />
          )}
        </div>
      </div>
    </div>
  );
}
