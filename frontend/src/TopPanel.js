import React from 'react'
import {buttonStyle} from './style'

/*
let buttonStyle = {
    border: "none",
    backgroundColor: "transparent",
    color: "inherit",
    fontSize: "inherit",
    cursor: "pointer"
}
*/

let newButtonStyle = Object.assign({}, buttonStyle, {borderRadius: "5px", padding: "5px", marginRight: "20px", fontSize: "25px"} )

function TopPanel(props) {
    //Generates a panel containing 'Revature', the users role, and a 'logout' and 'new' button
    return <div style={{ display: "flex", justifyContent: "space-between", fontSize: "23px",
        padding: "20px" }} >
        <div>Revature</div>
        <div>{`${props.client.role.role} Dashboard`}</div>
        <div>
            <button style={newButtonStyle}
                onClick={props.startNewAccountDialog}>+</button>
            <button style={newButtonStyle} onClick={props.logout} >Logout</button>
        </div>
    </div>
}

export { TopPanel }