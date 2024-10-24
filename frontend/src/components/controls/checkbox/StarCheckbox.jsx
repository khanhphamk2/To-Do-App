import { Checkbox } from "@headlessui/react";
import PropTypes from "prop-types";
export default function StarCheckbox({ onToggle, enabled }) {
  return (
    <Checkbox
      checked={enabled}
      onChange={onToggle}
      className={`relative flex items-center justify-center h-6 w-6 transition-colors`}
    >
      <svg
        strokeWidth={1.5}
        fill={enabled ? "currentColor" : "none"}
        stroke="currentColor"
        viewBox="0 0 24 24"
        xmlns="http://www.w3.org/2000/svg"
        aria-hidden="true"
        className="size-10 text-yellow-500"
      >
        <path
          strokeLinecap="round"
          strokeLinejoin="round"
          strokeWidth={enabled ? 0 : 2}
          d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z"
        />
      </svg>
    </Checkbox>
  );
}

StarCheckbox.propTypes = {
  onToggle: PropTypes.func.isRequired,
  enabled: PropTypes.bool.isRequired,
};
