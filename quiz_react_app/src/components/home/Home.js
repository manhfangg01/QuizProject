import VideoHomePage from '../../assets/video-homepage.mp4';

const Home = (props) => {
    return (
        <div className="homepage-container">
            
            <video
                
                
                autoPlay
                loop
                muted
            >
                <source type="video/mp4" src={VideoHomePage} />
            </video>
        </div>
    );
};

export default Home;