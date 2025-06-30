import { Fragment } from "react";
import { ProSidebar, Menu, MenuItem, SubMenu, SidebarHeader, SidebarFooter, SidebarContent } from "react-pro-sidebar";
import { FaTachometerAlt, FaGem, FaList, FaGithub, FaRegLaughWink, FaHeart } from "react-icons/fa";
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
            Hoi Dan It
          </div>
        </SidebarHeader>

        <SidebarContent>
          <Menu iconShape="circle">
            <MenuItem icon={<FaTachometerAlt />} suffix={<span className="badge red"></span>}></MenuItem>
            <MenuItem icon={<FaGem />}></MenuItem>
          </Menu>
          <Menu iconShape="circle">
            <SubMenu suffix={<span className="badge yellow">3</span>} icon={<FaRegLaughWink />}>
              <MenuItem>SubMenu 1</MenuItem>
              <MenuItem>SubMenu 2</MenuItem>
              <MenuItem>SubMenu 3</MenuItem>
            </SubMenu>
            <SubMenu prefix={<span className="badge gray">3</span>} icon={<FaHeart />}>
              <MenuItem>SubMenu 1</MenuItem>
              <MenuItem>SubMenu 2</MenuItem>
              <MenuItem>SubMenu 3</MenuItem>
            </SubMenu>
            <SubMenu icon={<FaList />}>
              <MenuItem>SubMenu 1 </MenuItem>
              <MenuItem>SubMenu 2 </MenuItem>
              <SubMenu title={`$SubMenu 3`}>
                <MenuItem>SubMenu 3.1 </MenuItem>
                <MenuItem>SubMenu 3.2 </MenuItem>
                <SubMenu title={`$SubMenu 3.3`}>
                  <MenuItem>SubMenu 3.3.1 </MenuItem>
                  <MenuItem>SubMenu 3.3.2 </MenuItem>
                  <MenuItem>SubMenu 3.3.3 </MenuItem>
                </SubMenu>
              </SubMenu>
            </SubMenu>
          </Menu>
        </SidebarContent>

        <SidebarFooter style={{ textAlign: "center" }}>
          <div
            className="sidebar-btn-wrapper"
            style={{
              padding: "20px 24px",
            }}
          >
            <a href="https://github.com/azouaoui-med/react-pro-sidebar" target="_blank" className="sidebar-btn" rel="noopener noreferrer">
              <FaGithub />
              <span style={{ whiteSpace: "nowrap", textOverflow: "ellipsis", overflow: "hidden" }}></span>
            </a>
          </div>
        </SidebarFooter>
      </ProSidebar>
    </Fragment>
  );
};

export default SideBar;
