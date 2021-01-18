import React from 'react'

let elemStyle = {
    padding: "7px",
    fontSize: "12px"
}

function SearchResult(props) {
    //A styled <div> element containing some text
    return <div style={{ padding: "10px", display: "flex", alignItems: "center", cursor: "pointer" }}>
        <div style={{ display: "flex", alignItems: "center" }}>
            <div style={{ backgroundColor: props.color, width: "20px", height: "50px" }}></div>
            <div style={{ fontSize: "17px", marginLeft: "5px" }}>{props.name}</div>
        </div>
        <div style={{ display: "flex", alignItems: "center", flexWrap: "wrap" }} >
            {props.items.map(str => {
                return <div style={elemStyle}>{str}</div>
            })}
        </div>
    </div>
}

export { SearchResult }