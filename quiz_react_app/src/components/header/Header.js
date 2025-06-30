import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import { NavLink, Link } from 'react-router-dom';

const Header =() => {
  return (
    <Navbar expand="lg" className="bg-body-tertiary">
      <Container>
        <NavLink className="navbar-brand" to="/">QuizProject</NavLink>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            {/* Thẻ link của thư viện đặc biệt hơn thẻ a của HTML là khi nhấn vào link sẽ không bị refresh */}
            {/* Thẻ NavLink của thư viện reactRouterDom tự động tích hợp lớp Active khi chuyển trang */}
            <NavLink to="/" className='nav-link'>Home</NavLink>   
            <NavLink to="/admins" className='nav-link'>Admin</NavLink>
            <NavLink to="/users" className='nav-link'>User</NavLink>
            {/* <Nav.Link href="/">Home</Nav.Link>
            <Nav.Link href="/users">Users</Nav.Link>
            <Nav.Link href="/admins">Admin</Nav.Link> */}
          </Nav>
          <Nav>
            <button className='btn-login'>Log in</button>
            <button className='btn-signup'>Sign up</button>
          {/* <NavDropdown title="Setting" id="basic-nav-dropdown">
              <NavDropdown.Item >Log in</NavDropdown.Item>
              <NavDropdown.Item >
                Log out
              </NavDropdown.Item>
              <NavDropdown.Item >Profile</NavDropdown.Item>
            </NavDropdown> */}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}

export default Header;