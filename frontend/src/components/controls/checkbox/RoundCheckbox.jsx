import { Checkbox } from "@headlessui/react";
import { CheckIcon } from "@heroicons/react/16/solid";
import PropTypes from "prop-types";

export default function RoundCheckbox({ onToggle, enabled }) {
  return (
    <Checkbox
      checked={enabled}
      onChange={onToggle}
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

RoundCheckbox.propTypes = {
  onToggle: PropTypes.func.isRequired,
  enabled: PropTypes.bool.isRequired,
};
