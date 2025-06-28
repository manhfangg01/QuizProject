import './App.scss';
import Header from './components/header/Header';
import { Link } from 'react-router-dom';
function App() {
  return (
    <div className="app-container">
      <Header/>
      {/* Thẻ link của thư viện đặc biệt hơn thẻ a của HTML là khi nhấn vào link sẽ không bị refresh */}
      <button>
        <Link to="/users">go to user page</Link>
      </button>
      <button><Link to="/admins">go to admin page</Link></button> 
    </div>
  );
}

export default App;
