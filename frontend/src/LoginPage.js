import React, { useState } from "react";
import {inputStyle, inputWrapperStyle, buttonStyle} from './style'

function LoginPage(props) {
    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")

    //Handles the event of the username input field changing
    const usernameInput = (ev) => {
        setUsername(ev.target.value)
    }

    //Handles the event of the password input field changing
    const passwordInput = (ev) => {
        setPassword(ev.target.value)
    }

    //Returns the login form
    return <div style={{ display: "flex", justifyContent: "center", alignItems: "center" }} >
        <div >
            <h2 style={{ marginBottom: "20px" }}>Login</h2>
            <div style={inputWrapperStyle} >
                <p style={{ marginRight: "5px" }} >Username</p>
                <input style={inputStyle} onChange={usernameInput} />
            </div>
            <div style={inputWrapperStyle} >
                <p style={{ marginRight: "5px" }} >Password</p>
                <input style={inputStyle} onChange={passwordInput} />
            </div>
            <button
                style={buttonStyle}
                onClick={() => props.tryLogin(username, password)}>Go</button>
            <h3 style={{color: "red"}}>{props.errorMessage}</h3>
        </div>

    </div>

}

export { LoginPage }
