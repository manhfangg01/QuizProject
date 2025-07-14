import { useEffect, useState } from "react";
import { Card } from "react-bootstrap";
import { getAvarageDuration } from "../../services/ResultServices";
import { toast } from "react-toastify";
export default function AvarageDuration() {
  const [avgDuration, setAvgDuration] = useState("Đang tải...");
  const fetchAvarageDuration = async () => {
    try {
      const response = await getAvarageDuration();
      if (response) {
        setAvgDuration(response);
      } else {
        toast.warning("Gọi API Duration thất bại !");
      }
    } catch (err) {
      toast.warning(err);
    }
  };

  useEffect(() => {
    fetchAvarageDuration();
  }, []);

  return (
    <Card className="dashboard-text shadow-sm p-3 text-center bg-light">
      <h5>🕒 Thời gian làm bài trung bình</h5>
      <h4 className="mt-2">⏱ {avgDuration}</h4>
    </Card>
  );
}
