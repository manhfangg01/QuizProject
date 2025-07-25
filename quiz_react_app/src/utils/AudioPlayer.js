import React, { useRef, useState, useEffect } from "react";
import "./AudioPlayer.scss";
import { FaPlay, FaPause, FaVolumeUp, FaVolumeMute, FaVolumeDown } from "react-icons/fa";
import { IoMdSpeedometer } from "react-icons/io";

const AudioPlayer = ({ audioUrl }) => {
  console.log("CHECK auido", audioUrl);
  const audioRef = useRef(null);
  const [isPlaying, setIsPlaying] = useState(false);
  const [currentTime, setCurrentTime] = useState(0);
  const [duration, setDuration] = useState(0);
  const [volume, setVolume] = useState(0.7);
  const [isMuted, setIsMuted] = useState(false);
  const [playbackRate, setPlaybackRate] = useState(1);
  const [showSpeedOptions, setShowSpeedOptions] = useState(false);

  // Cập nhật thời gian hiện tại
  useEffect(() => {
    const audio = audioRef.current;
    if (!audio) return;

    const updateTime = () => setCurrentTime(audio.currentTime);
    const updateDuration = () => setDuration(audio.duration || 0);

    audio.addEventListener("timeupdate", updateTime);
    audio.addEventListener("loadedmetadata", updateDuration);
    audio.addEventListener("ended", () => setIsPlaying(false));

    return () => {
      audio.removeEventListener("timeupdate", updateTime);
      audio.removeEventListener("loadedmetadata", updateDuration);
      audio.removeEventListener("ended", () => setIsPlaying(false));
    };
  }, []);

  // Áp dụng volume khi thay đổi
  useEffect(() => {
    if (audioRef.current) {
      audioRef.current.volume = isMuted ? 0 : volume;
    }
  }, [volume, isMuted]);

  // Áp dụng tốc độ phát
  useEffect(() => {
    if (audioRef.current) {
      audioRef.current.playbackRate = playbackRate;
    }
  }, [playbackRate]);

  const togglePlay = () => {
    const audio = audioRef.current;
    if (!audio) return;

    if (isPlaying) {
      audio.pause();
    } else {
      audio.play();
    }
    setIsPlaying(!isPlaying);
  };

  const handleTimeChange = (e) => {
    const newTime = parseFloat(e.target.value);
    setCurrentTime(newTime);
    if (audioRef.current) {
      audioRef.current.currentTime = newTime;
    }
  };

  const handleVolumeChange = (e) => {
    const newVolume = parseFloat(e.target.value);
    setVolume(newVolume);
    setIsMuted(newVolume === 0);
  };

  const toggleMute = () => {
    setIsMuted(!isMuted);
  };

  const formatTime = (time) => {
    if (isNaN(time)) return "0:00";
    const minutes = Math.floor(time / 60);
    const seconds = Math.floor(time % 60);
    return `${minutes}:${seconds < 10 ? "0" : ""}${seconds}`;
  };

  const playbackRates = [0.5, 0.75, 1, 1.25, 1.5, 2];

  return (
    <div className="audio-player">
      {/* Play/Pause button */}
      <button className="control-button" onClick={togglePlay}>
        {isPlaying ? <FaPause /> : <FaPlay />}
      </button>

      {/* Current time */}
      <span className="time-display">{formatTime(currentTime)}</span>

      {/* Progress bar */}
      <input type="range" className="progress-bar" value={currentTime} max={duration || 100} onChange={handleTimeChange} />

      {/* Duration */}
      <span className="time-display">{formatTime(duration)}</span>

      {/* Volume control */}
      <div className="volume-control">
        <button className="control-button" onClick={toggleMute}>
          {isMuted ? <FaVolumeMute /> : volume > 0.5 ? <FaVolumeUp /> : <FaVolumeDown />}
        </button>
        <input type="range" className="volume-slider" value={isMuted ? 0 : volume} min="0" max="1" step="0.01" onChange={handleVolumeChange} />
      </div>

      {/* Playback speed */}
      <div className="speed-control">
        <button className="control-button" onClick={() => setShowSpeedOptions(!showSpeedOptions)}>
          <IoMdSpeedometer />
          <span>{playbackRate}x</span>
        </button>
        {showSpeedOptions && (
          <div className="speed-options dropdown">
            {playbackRates.map((rate) => (
              <button
                key={rate}
                className={playbackRate === rate ? "active" : ""}
                onClick={() => {
                  setPlaybackRate(rate);
                  setShowSpeedOptions(false);
                }}
              >
                {rate}x
              </button>
            ))}
          </div>
        )}
      </div>

      <audio ref={audioRef} src={audioUrl} />
    </div>
  );
};

export default AudioPlayer;
