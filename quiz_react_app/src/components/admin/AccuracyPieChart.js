import { PieChart, pieArcLabelClasses } from "@mui/x-charts/PieChart";
import { useEffect, useState } from "react";
import { getAccuracy } from "../../services/AnswerService";
import { toast } from "react-toastify";

// Dữ liệu mẫu: bạn sẽ thay bằng props hoặc dữ liệu từ API

export default function AccuracyPieChart() {
  const [data, setData] = useState([]);
  const size = {
    width: 250,
    height: 250,
  };

  const fetchDataAccuracy = async () => {
    try {
      const response = await getAccuracy();
      if (response.statusCode === 200) {
        setData([
          { label: "Đúng", value: response.data.correct },
          { label: "Sai", value: response.data.incorrect },
        ]);
      } else {
        toast.warning("Gọi API Accuracy thất bại !");
      }
    } catch (err) {
      toast.warning(err);
    }
  };

  useEffect(() => {
    fetchDataAccuracy();
  }, []);

  return (
    <PieChart
      series={[
        {
          data: data,
          arcLabel: (item) => `${item.label}: ${item.value}%`,
          arcLabelMinAngle: 35,
          arcLabelRadius: "60%",
        },
      ]}
      sx={{
        [`& .${pieArcLabelClasses.root}`]: {
          fontWeight: "bold",
        },
      }}
      {...size}
    />
  );
}
