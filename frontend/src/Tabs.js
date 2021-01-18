import React, {useState} from 'react'

//Reusable tabs component
//Takes as input a list of strings used in the tabs
//and a function to call when a tab is selected
function Tabs(props){
    const [currentIdx, setCurrentIdx] = useState(0)

    let itemsJSX = props.items.map( (str, idx) => {
        let bg = null;
        let color = null
        if( idx == currentIdx ){
            bg = "grey"
            color = "inherit"
        }else{
            bg = "#505050"
            color = "grey"
        }
        return <div style={{backgroundColor: bg, color: color, borderRadius: "5px", padding: "5px"}} onClick={() => {
            setCurrentIdx( idx )
            props.updateSelected( idx )
        }} >{str}</div>
    })

    return <div style={{display: "flex", cursor: "pointer", justifyContent: "center"}} >{itemsJSX}</div>
}

export {Tabs}