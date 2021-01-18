import React from 'react'

//Reusable DetailPanel for displaying and updating Users and Account
//Input:
//  color: css color to use
//  content: JSX used in this JSX
//  name: text to display
//  toggleMode: function that switches the mode
//  modeButtonText: text to display on the mode changing button
//  hasEditPermission: whether to display the 'edit' button
function DetailPanel(props) {
    //The optional edit button
    let button = null
    if( props.hasEditPermission ){
        button = <button style={{
            marginLeft: "20px",
            padding: "10px",
            fontSize: "20px",
            backgroundColor: "grey", color: "inherit", border: "none",
            borderRadius: "10px", cursor: "pointer"
        }} onClick={props.toggleMode}
        >{props.modeButtonText}</button>
    }else{
        button = <div></div>
    }
    //Generate JSX
    return <div style={{ display: "flex", flexDirection: "column" }}>
        <div style={{ display: "flex", alignItems: "center" }}>
            <div style={{ width: "20px", height: "60px", backgroundColor: props.color, marginRight: "15px" }}></div>
            <h2>{`${props.name} detail`}</h2>
            {button}
        </div>
        <div style={{ display: "flex", flexDirection: "column", flexWrap: "wrap" }}>
            {props.content}
        </div>
    </div>
}

export {DetailPanel}