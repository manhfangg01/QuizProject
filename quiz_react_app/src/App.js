import './App.scss';
import Header from './components/header/Header';
import { Link,Outlet } from 'react-router-dom';
function App() {
  return (
    <div className="app-container">
      <div className='header-container'>
        <Header/>
      </div>
      <div className='main-container'>
        <div className='sidenav-container'>

        </div>
        <div className='app-container'>
          <Outlet/>   {/*Component child router emerge here */}
        </div>
      </div>
     

      
      
    </div>
  );
}

export default App;
