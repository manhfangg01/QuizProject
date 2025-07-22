import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
import { getAuth, GoogleAuthProvider, signInWithPopup } from "firebase/auth";

const firebaseConfig = {
  apiKey: "AIzaSyBdLUbKCKEesZ6z209IOridEks2gEl6lt4",
  authDomain: "quizproject-e9bc5.firebaseapp.com",
  projectId: "quizproject-e9bc5",
  storageBucket: "quizproject-e9bc5.firebasestorage.app",
  messagingSenderId: "81378515734",
  appId: "1:81378515734:web:23d275f9e85e43d3ba0e37",
  measurementId: "G-T5QCVT0CW4",
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);

const provider = new GoogleAuthProvider();

const auth = getAuth();

export const authWithGoogle = async () => {
  let user = null;

  await signInWithPopup(auth, provider)
    .then((result) => {
      user = result.user;
    })
    .catch((err) => {
      console.log(err);
    });
  return user;
};
