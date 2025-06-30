import { Fragment } from "react";
import { ProSidebar, Menu, MenuItem, SubMenu, SidebarHeader, SidebarFooter, SidebarContent } from "react-pro-sidebar";
import { FaReact, FaList, FaRegQuestionCircle, FaBandcamp, FaUserGraduate, FaBook } from "react-icons/fa";

import { MdDashboard, MdQuiz } from "react-icons/md";
import sidebarBg from "../../assets/bg2.png";
import "react-pro-sidebar/dist/css/styles.css";

const SideBar = ({ image, collapsed, rtl, toggled, handleToggleSidebar }) => {
  return (
    <Fragment>
      <ProSidebar image={sidebarBg} rtl={rtl} collapsed={collapsed} toggled={toggled} breakPoint="md" onToggle={handleToggleSidebar}>
        <SidebarHeader>
          <div
            style={{
              padding: "24px",
              textTransform: "uppercase",
              fontWeight: "bold",
              fontSize: 14,
              letterSpacing: "1px",
              overflow: "hidden",
              textOverflow: "ellipsis",
              whiteSpace: "nowrap",
            }}
          >
            <FaReact color={"00bfff"} size={"2.5rem"} />
            <span style={{ visibility: collapsed ? "hidden" : "visible" }}>Hoi Dan It</span>
          </div>
        </SidebarHeader>

        <SidebarContent>
          <Menu iconShape="circle">
            <MenuItem icon={<MdDashboard />}>Dashboard</MenuItem>
          </Menu>
          <Menu iconShape="circle">
            <SubMenu icon={<FaList />} title={"Features"}>
              <MenuItem icon={<MdQuiz />}>Quizzes</MenuItem>
              <MenuItem icon={<FaRegQuestionCircle />}>Questions</MenuItem>
              <MenuItem icon={<FaBook />}>Options</MenuItem>
              <MenuItem icon={<FaBandcamp />}>Results</MenuItem>
              <MenuItem icon={<FaUserGraduate />}>Users</MenuItem>
            </SubMenu>
          </Menu>
        </SidebarContent>

        <SidebarFooter style={{ textAlign: "center" }}>
          <div style={{ padding: "10px 0", fontSize: "12px", color: "#ccc" }}>© 2025 ManhFangg Academy</div>
        </SidebarFooter>
      </ProSidebar>
    </Fragment>
  );
};

export default SideBar;
