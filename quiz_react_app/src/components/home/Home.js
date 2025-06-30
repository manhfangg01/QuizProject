import VideoHomePage from "../../assets/video-homepage.mp4";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCheck } from "@fortawesome/free-solid-svg-icons";
const Home = (props) => {
  return (
    <div className="homepage-container">
      <video autoPlay loop muted>
        <source type="video/mp4" src={VideoHomePage} />
      </video>

      <div className="homepage-content">
        <div className="title-1">There're better way to ask</div>
        <div className="title-2 title-highlight">You don't want to make a boring form. And your audiences won't answer one. Create a typeform instead and make everyone happy.</div>
        <div className="title-3">
          <button className="btn btn-dark">Get's started. It's free.</button>
        </div>
        <div className="content-notice">
          <div className="notice-1">
            <FontAwesomeIcon icon={faCheck} className="icon" />
            No credit card required
          </div>
          <div className="notice-2">
            <FontAwesomeIcon icon={faCheck} className="icon" />
            No time limit on Free plan
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home;
