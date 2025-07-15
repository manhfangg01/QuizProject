// // StatisticsPage.jsx
// import React, { useEffect, useState } from "react";
// import { Tabs, Tab, Card, Button, Form, Row, Col } from "react-bootstrap";
// import { LineChart, Line, XAxis, YAxis, Tooltip, CartesianGrid } from "recharts";
// import { fetchStatistics } from "../../services/StatisticService";

// const StatCard = ({ label, value }) => (
//   <Card className="text-center mb-3">
//     <Card.Body>
//       <div>{label}</div>
//       <strong>{value}</strong>
//     </Card.Body>
//   </Card>
// );

// const SkillStats = ({ data }) => (
//   <Row>
//     <Col md={4}>
//       <StatCard label="Số đề đã làm" value={`${data.totalExams} đề thi`} />
//     </Col>
//     <Col md={4}>
//       <StatCard label="Độ chính xác" value={`${data.accuracy.toFixed(2)}%`} />
//     </Col>
//     <Col md={4}>
//       <StatCard label="Thời gian trung bình" value={data.avgTime} />
//     </Col>
//     <Col md={4}>
//       <StatCard label="Điểm trung bình" value={`${data.avgScore}/9.0`} />
//     </Col>
//     <Col md={4}>
//       <StatCard label="Điểm cao nhất" value={`${data.maxScore}/9.0`} />
//     </Col>
//   </Row>
// );

// const StatisticsPage = () => {
//   const [filterDays, setFilterDays] = useState(30);
//   const [data, setData] = useState(null);

//   useEffect(() => {
//     loadStats();
//   }, []);

//   const loadStats = async () => {
//     const response = await fetchStatistics(filterDays);
//     setData(response);
//   };

//   const handleSearch = () => {
//     loadStats();
//   };

//   const handleClear = () => {
//     setFilterDays(30);
//     loadStats();
//   };

//   if (!data) return <div>Loading...</div>;

//   return (
//     <div className="container mt-4">
//       <h2>Thống kê kết quả luyện thi</h2>

//       <div className="d-flex align-items-center gap-2 mb-4">
//         <Form.Select value={filterDays} onChange={(e) => setFilterDays(e.target.value)} style={{ width: "150px" }}>
//           <option value="7">7 ngày</option>
//           <option value="30">30 ngày</option>
//           <option value="90">90 ngày</option>
//         </Form.Select>
//         <Button onClick={handleSearch}>Search</Button>
//         <Button variant="secondary" onClick={handleClear}>
//           Clear
//         </Button>
//       </div>

//       <Row>
//         <Col md={4}>
//           <StatCard label="Số đề đã làm" value={data.totalExams} />
//         </Col>
//         <Col md={4}>
//           <StatCard label="Thời gian luyện thi" value={`${data.totalMinutes} phút`} />
//         </Col>
//         <Col md={4}>
//           <StatCard label="Điểm mục tiêu" value={<a href="/set-goal">Tạo ngay</a>} />
//         </Col>
//       </Row>

//       <Tabs defaultActiveKey="listening" className="mt-4">
//         <Tab eventKey="listening" title="Listening">
//           <SkillStats data={data.skillStats.listening} />
//         </Tab>
//         <Tab eventKey="reading" title="Reading">
//           <SkillStats data={data.skillStats.reading} />
//         </Tab>
//         <Tab eventKey="writing" title="Writing">
//           <SkillStats data={data.skillStats.writing} />
//         </Tab>
//         <Tab eventKey="speaking" title="Speaking">
//           <SkillStats data={data.skillStats.speaking} />
//         </Tab>
//       </Tabs>

//       <div className="mt-5">
//         <h5>Biểu đồ độ chính xác theo thời gian</h5>
//         <LineChart width={800} height={300} data={data.accuracyOverTime} margin={{ top: 10, right: 30, left: 0, bottom: 0 }}>
//           <CartesianGrid strokeDasharray="3 3" />
//           <XAxis dataKey="date" />
//           <YAxis />
//           <Tooltip />
//           <Line type="monotone" dataKey="accuracy" stroke="#FF6384" strokeWidth={2} />
//         </LineChart>
//       </div>
//     </div>
//   );
// };

// export default StatisticsPage;
