import React, { useEffect, useState } from "react";
import { ComboBox } from "./ComboBox";
import { DetailPanel } from "./DetailPanel";
import { buttonStyle } from "./style";

let inputWrapperStyle = { display: "flex", alignItems: "center" }
let inputStyle = { color: "inherit", backgroundColor: "gray", border: "none" }

function AccountDetailPanel(props) {
    //The user input
    const [amount, setAmount] = useState(props.account.amount)
    const [typeIdx, setTypeIdx] = useState(-1)
    const [statusIdx, setStatusIdx] = useState(-1)

    //The mode
    const [mode, setMode] = useState("View")

    //The data loaded from server
    const [types, setTypes] = useState([])
    const [statuss, setStatuss] = useState([])

    //The error and success message
    const [message, setMessage] = useState("")
    const [messageColor, setMessageColor] = useState("")

    //Loads the types from the server
    const loadTypes = () => {
        //Fetch all types
        fetch('/BankAPI/accountTypes', {
            method: "GET"
        }).then(response => {
            //Check response status code
            if (!response.ok) {
                throw new Error("Could not fetch types")
            }
            //Parse response json
            return response.json()
        }).then(json => {
            //set types and typeIdx
            findTypeIdx(json)
        }).catch(error => {
            //Just write error to console
            console.log(error)
        })
    }

    //Loads the statuss from the server
    const loadStatuss = () => {
        //Fetch all statuss
        fetch('/BankAPI/accountStatus', {
            method: "GET"
        }).then(response => {
            //Check response status code
            if (!response.ok) {
                throw new Error("Could not fetch statuss")
            }
            //Parse response json
            return response.json()
        }).then(json => {
            //set statuss andstatusIdx 
            findStatusIdx(json)
        }).catch(error => {
            //Just write error to console
            console.log(error)
        })
    }

    const findTypeIdx = (types) => {
        //Default index when types are not loaded
        let newIdx = -1
        //Find the typeIdx of the account
        for (let idx = 0; idx < types.length; idx++) {
            if (props.account.type.id == types[idx].id) {
                newIdx = idx
                break
            }
        }
        setTypes(types)
        setTypeIdx(newIdx)
    }

    const findStatusIdx = (statuss) => {
        //Default index when statuss are not loaded
        let newIdx = -1
        //Find the statusIdx of the account
        for (let idx = 0; idx < statuss.length; idx++) {
            if (props.account.status.id == statuss[idx].id) {
                newIdx = idx
                break
            }
        }
        setStatuss(statuss)
        setStatusIdx(newIdx)
    }

    const toMessageMode = (newMessage, color) => {
        setMessage(newMessage)
        setMessageColor(color)
        setMode("Message")
    }

    //Updates the Account
    const submit = () => {
        if( typeIdx == -1 || statusIdx == -1 ){
            toMessageMode("Please select a type and status", "red")
            return
        }

        //PUT request to update the account
        fetch('/BankAPI/accounts', {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                id: props.account.id,
                amount: parseFloat( amount ),
                typeId: types[typeIdx].id,
                statusId: statuss[statusIdx].id
            })
        }).then((response) => {
            if (!response.ok) {
                throw new Error("Bad status code while updating account")
            }
            return response.json()
        }).then((json) => {
            toMessageMode("Account updated", "green")
        }).catch(error => {
            toMessageMode(error + "", "red")
        })
    }

    //Load types and statuss once after the first render
    useEffect(()=>{
        loadStatuss()
        loadTypes()
    }, [])

    let content = null;
    if (mode == "Edit") {
        //We want to display a form for updating the selected account

        //The input for amount
        let amountJSX = <div style={inputWrapperStyle} >
            <p style={{ marginRight: "5px" }} >Balance:</p>
            <input style={inputStyle} value={amount} onChange={(ev) => {
                setAmount(ev.target.value)
            }} />
        </div>

        //The type drop down selector
        let typeJSX = <ComboBox
            items={types.map(type => type.type)}
            updateSelection={(idx) => {
                setTypeIdx(idx)
            }}
            initIdx={typeIdx}
        />

        //The status drop down selector
        let statusJSX = <ComboBox
            items={statuss.map(status => status.status)}
            updateSelection={(idx) => {
                setStatusIdx(idx)
            }}
            initIdx={statusIdx}
        />

        //The button to update
        let submitJSX = <button
            style={buttonStyle}
            onClick={() => { submit() }}>Save</button>

        //Save to content
        content = [amountJSX, typeJSX, statusJSX, submitJSX]
    } else if(mode == "View") {
        //View mode. We want to just display the data
        content = [<div>
            <div>{`Status: ${props.account.status.status}`}</div>
            <div>{`Type: ${props.account.type.type}`}</div>
            <div>{`Balance: ${props.account.amount}`}</div>
        </div>]
        
    }else if( mode == "Message" ){
        content = <h3 style={{color: messageColor}} >{message}</h3>
    }

    //The generated JSX
    return <DetailPanel
        name={"Account"}
        color={"blue"}
        toggleMode={() => {
            if (mode == "View") {
                setMode("Edit")
            } else if (mode == "Edit") {
                setMode("View")
            } else if( mode == "Message" ){
                setMode("Edit")
            }
        }}
        modeButtonText={mode == "View" ? "Edit" : ( mode == "Edit" ? "View" : "Return" ) }
        content={content}
        hasEditPermission={props.hasEditPermission}
    />

}

export { AccountDetailPanel }