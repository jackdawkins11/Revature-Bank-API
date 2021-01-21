import React, { useEffect, useState } from "react";
import { render } from "react-dom";
import { HomePage } from './HomePage';
import { LoginPage } from './LoginPage';

function App(props) {
    //We store an error message for LoginPage
    //and a User. User == null implies we are logged out
    const [client, setClient] = useState(null)
    const [errorMessage, setErrorMessage] = useState("")

    useEffect(() => {
        //This will run once after the first render.
        //We want to check if the user is logged in once
        checkSession()
    }, [])

    const checkSession = () => {
        //Make the POST request
        fetch('/BankAPI/checkSession', {
            method: "POST"
        }).then((response) => {
            //Reject promise based on status code
            if (!response.ok) {
                throw new Error(`Couldn't login. Status code: ${response.status}`)
            }
            //Resolve with json payload
            return response.json()
        }).then((json) => {
            //On success, the server returns the logged in user
            //Save it
            setClient(json)
            console.log(`Login successfull. Client: ${json}`)
        }).catch(error => {
            //Write the result of the request
            console.log(error)
            //Set client = null
            setClient(null)
        })
    }

    //Try to login to the server with the given
    //credentials
    const tryLogin = (username, password) => {
        //Make the POST request
        fetch('/BankAPI/login', {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username: username, password: password })
        }).then((response) => {
            //Reject promise based on status code
            if (!response.ok) {
                throw new Error(`Couldn't login. Status code: ${response.status}`)
            }
            //Resolve with json payload
            return response.json()
        }).then((json) => {
            //On success, the server returns the logged in user
            //Save it
            //We also want to reset the error message, as there could be one right now
            setClient(json)
            setErrorMessage("")
            console.log(`Login successfull. Client: ${json}`)
        }).catch(error => {
            //Write the error
            //Leave client == null
            //Set error message
            setErrorMessage(error + "")
            console.log(error)
        })
    }

    //End the session with the server
    const tryLogout = () => {
        console.log("Trying logout")
        //Make POST request
        fetch('/BankAPI/logout', {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            }
        }).then((response) => {
            //Check status code
            //set client == null on success
            if (!response.ok) {
                console.log("There was an error logging out")
            } else {
                console.log("Successfully logged out")
            }
            setClient(null)
        })
    }

    //Render LoginPage or HomePage, depending on whether we are logged in
    let content = null
    if (client == null) {
        content = <LoginPage tryLogin={tryLogin} errorMessage={errorMessage} />
    } else {
        content = <HomePage client={client} tryLogout={tryLogout} />
    }
    return <div style={{
        position: "fixed", left: 0, top: 0, width: "100%", height: "100%",
        backgroundColor: "#333333", color: "#c3c3c3", fontFamily: "sans-serif"
    }}>
        {content}
    </div>
}

render(<App />, document.getElementById("root"));