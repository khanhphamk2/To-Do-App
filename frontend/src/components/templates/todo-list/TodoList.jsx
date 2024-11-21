import { useState, useEffect } from "react";
import Task from "../task/Task";
import TaskEdit from "../task/TaskEdit";
import { DragDropContext, Draggable, Droppable } from "react-beautiful-dnd";
import { PlusIcon } from "@heroicons/react/24/outline";
import {
  getAllTask,
  createTask,
  deleteTask,
  updateTask,
  changeTaskCompleted,
  changeTaskImportant,
} from "../../../api/task.api";

export default function TodoList() {
  const [tasks, setTasks] = useState([]);
  const [isAdding, setIsAdding] = useState(false);
  const [newTask, setNewTask] = useState(null);

  useEffect(() => {
    getData();
  }, []);

  const getData = async () => {
    getAllTask()
      .then((tasks) => setTasks(tasks))
      .catch((error) => {
        console.error("Error fetching tasks:", error);
        setTasks();
      });
  };

  const showEdit = (id) => {
    checkOtherShowEdit();
    const task = tasks.find((task) => task.id === id);
    if (task) {
      task.editMode = true;
      setTasks([...tasks]);
    }
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

  const addTask = () => {
    closeEdits();
    setNewTask({
      id: tasks.length + 1,
      title: "",
      description: "",
      date: new Date(),
      time: "00:00",
      important: false,
      completed: false,
      editMode: true,
    });
    setIsAdding(true);
  };

  const saveTask = (taskData) => {
    const taskToAdd = { ...newTask, ...taskData, editMode: false };

    const data = {
      title: taskToAdd.title,
      description: taskToAdd.description,
      date: taskToAdd.date,
      time: taskToAdd.time,
    };

    createTask(data)
      .then(() => {
        setTasks((prevTasks) => [...prevTasks, taskToAdd]);
        setIsAdding(false);
        setNewTask(null);
      })
      .catch((error) => console.error("Error creating task:", error));
  };

  const closeEdits = () => {
    setTasks((prevTasks) =>
      prevTasks.map((task) => ({ ...task, editMode: false }))
    );
    setIsAdding(false);
  };

  const handleDeleteTask = (id) => {
    setTasks((prevTasks) => prevTasks.filter((task) => task.id !== id));
  };

  const removeTask = (id) => {
    deleteTask(id)
      .then(() => handleDeleteTask(id))
      .catch((error) => console.error("Error deleting task:", error));

    handleDeleteTask();
  };

  const updateTaskData = (data) => {
    const updatedData = {
      title: data.title,
      description: data.description,
      date: data.date,
      time: data.time,
    };

    updateTask(data.id, updatedData)
      .then(() => console.log("Task updated!"))
      .catch((error) => console.error("Error updating task:", error));
  };

  // const toggleTaskField = (id, field) => {
  //   setTasks((prevTasks) =>
  //     prevTasks.map((task) =>
  //       task.id === id ? { ...task, [field]: !task[field] } : task
  //     )
  //   );
  // };

  const toggleTaskComplete = (id, isCompleted) => {
    // Optimistically update the task list in the state before making the API call
    updateTaskState(id, { isCompleted: !isCompleted });

    // Make the API call to update the task status
    changeTaskCompleted(id, { value: !isCompleted })
      .then(() => getAllTask())
      .catch((error) => {
        console.error("Error toggling task completion:", error);

        // If there is an error, revert the change to the completion status
        updateTaskState(id, { isCompleted });
      });
  };

  const toggleTaskImportant = (id, isImportant) => {
    changeTaskImportant(id, { value: !isImportant })
      .then(() => {
        getAllTask();
        updateTaskState(id, { isImportant: !isImportant });
      })
      .catch((error) =>
        console.error("Error toggling task importance:", error)
      );
  };

  const updateTaskState = (id, updatedFields) => {
    setTasks((prevTasks) =>
      prevTasks.map((task) =>
        task.id === id ? { ...task, ...updatedFields } : task
      )
    );
  };

  const onDragEnd = (result) => {
    if (!result.destination) return;

    const reorderedTasks = [...tasks];
    const [movedTask] = reorderedTasks.splice(result.source.index, 1);
    reorderedTasks.splice(result.destination.index, 0, movedTask);
    setTasks(reorderedTasks);
  };

  const renderTask = (task, provided) => (
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
          onCancel={closeEdits}
          onSave={saveTask}
          onRemove={removeTask}
          onUpdate={updateTaskData}
        />
      ) : (
        <Task
          id={task.id}
          title={task.title}
          description={task.description}
          date={task.date}
          time={task.time}
          completed={task.completed}
          important={task.important}
          onEdit={() => showEdit(task.id)}
          onToggleComplete={() => toggleTaskComplete(task.id, task.completed)}
          onToggleImportant={() => toggleTaskImportant(task.id, task.important)}
        />
      )}
    </div>
  );

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
                  <PlusIcon
                    className="size-4"
                    stroke="currentColor"
                    fill="none"
                    strokeWidth="2.5"
                  />
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
                      {(provided) => renderTask(task, provided)}
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
              onSave={saveTask}
              onCancel={closeEdits}
              onRemove={removeTask}
              onUpdate={updateTaskData}
              addMode
            />
          )}
        </div>
      </div>
    </div>
  );
}
