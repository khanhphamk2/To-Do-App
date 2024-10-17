import { Checkbox } from "@headlessui/react";
import { CheckIcon } from "@heroicons/react/16/solid";
import { useState } from "react";

export default function RoundCheckbox() {
  const [enabled, setEnabled] = useState(true);

  return (
    <Checkbox
      checked={enabled}
      onChange={setEnabled}
      className={`relative flex items-center justify-center h-6 w-6 rounded-full ${
        enabled
          ? "bg-black hover:bg-gray-600 ring-white/10"
          : "bg-white/10 hover:bg-gray-300 ring-black/10"
      } ring-1 ring-inset transition-colors`}
    >
      {enabled && <CheckIcon className="absolute text-white h-4 w-4" />}
    </Checkbox>
  );
}
