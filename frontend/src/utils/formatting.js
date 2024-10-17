export const timeDiffFromNow = (timeString) => {
  const [hours, minutes] = timeString.split(':').map(Number);

  const now = new Date();

  const inputTime = new Date();
  inputTime.setHours(hours, minutes, 0, 0);

  const diffMilliseconds = inputTime - now;

  const diffSeconds = Math.floor(diffMilliseconds / 1000);
  const diffMinutes = Math.floor(diffSeconds / 60);
  const diffHours = Math.floor(diffMinutes / 60);

  return {
    diffMilliseconds,
    diffSeconds,
    diffMinutes,
    diffHours,
  };
};
