import { PieChart, pieArcLabelClasses } from "@mui/x-charts/PieChart";
import { useEffect, useState } from "react";
import { getAccuracy } from "../../services/AnswerService";
import { toast } from "react-toastify";

export default function AccuracyPieChart() {
  const [data, setData] = useState([]);
  const size = {
    width: 250,
    height: 250,
  };

  const fetchDataAccuracy = async () => {
    try {
      const response = await getAccuracy();
      console.log("Check accuracy", response);
      if (response.statusCode === 200) {
        const { correct, incorrect, skipped } = response.data;
        const total = correct + incorrect + skipped;

        if (total === 0) {
          setData([]);
          return;
        }

        setData([
          { label: "Đúng", value: correct },
          { label: "Sai", value: incorrect },
          { label: "Bỏ qua", value: skipped },
        ]);
      } else {
        toast.warning("Gọi API Accuracy thất bại !");
      }
    } catch (err) {
      toast.warning("Lỗi khi gọi API: " + err.message);
    }
  };

  useEffect(() => {
    fetchDataAccuracy();
  }, []);

  // Tính tổng để dùng trong arcLabel
  const total = data.reduce((sum, item) => sum + item.value, 0);

  return (
    <PieChart
      series={[
        {
          data: data,
          arcLabel: (item) => `${item.label}: ${item.value} (${((item.value / total) * 100).toFixed(1)}%)`,
          arcLabelMinAngle: 25,
          arcLabelRadius: "60%",
        },
      ]}
      sx={{
        [`& .${pieArcLabelClasses.root}`]: {
          fontWeight: "bold",
          fontSize: 12,
        },
      }}
      {...size}
    />
  );
}
