import React, { useEffect, useState } from "react";
import { ComboBox } from './ComboBox'
import {DetailPanel} from './DetailPanel'
import { buttonStyle, inputStyle, inputWrapperStyle } from "./style";

function UserDetailPanel(props) {
    //The userinput
    const [username, setUsername] = useState(props.user.username)
    const [firstName, setFirstName] = useState(props.user.firstName)
    const [lastName, setLastName] = useState(props.user.lastName)
    const [email, setEmail] = useState(props.user.email)
    const [roleIdx, setRoleIdx] = useState(-1)
    const [password, setPassword] = useState(props.user.password)

    //Data loaded from server
    const [roles, setRoles] = useState([])

    //The mode
    const [mode, setMode] = useState("View")

    //For message mode
    const [message, setMessage] = useState("")
    const [messageColor, setMessageColor] = useState("")

    //Loads the roles from the server
    function loadRoles() {
        //Fetch all roles
        fetch('/BankAPI/roles', {
            method: "GET"
        }).then(response => {
            //Check response status code
            if (!response.ok) {
                throw new Error("Could not fetch Roles")
            }
            //Parse response json
            return response.json()
        }).then(json => {
            //Save the roles and find the roleIdx
            initRoles(json)
        }).catch(error => {
            //Just write error to console
            console.log(error)
        })
    }

    //Called once roles are loaded
    //Sets the roles and finds the correct roleIdx
    const initRoles = (roles) => {
        let newIdx = -1
        for(let idx = 0; idx < roles.length; idx++){
            if(props.user.role.id == roles[idx].id){
                newIdx = idx
                break
            }
        }
        setRoleIdx(newIdx)
        setRoles(roles)
    }

    //Changes mode to message mode with the given message and color
    const toMessageMode = (newMessage, color) => {
        setMessage(newMessage)
        setMessageColor(color)
        setMode("Message")
    }

    //Updates the User
    function submit() {
        if( roleIdx == -1 ){
            toMessageMode("Please specify a role", "red")
            return
        }

        //PUT request to update the user
        fetch('/BankAPI/users', {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                userId: props.user.id,
                username: username,
                password: password,
                firstName: firstName,
                lastName: lastName,
                email: email,
                roleId: roles[roleIdx].id
            })
        }).then((response) => {
            if (!response.ok) {
                throw new Error("Bad status code while updating user: " + response.status)
            }
            return response.json()
        }).then((json) => {
            toMessageMode("User updated", "green")
        }).catch(error => {
            toMessageMode(error + "", "red")
        })
    }

    //Load the roles once after the first render
    useEffect(() => {
        loadRoles()
    }, [])

    let content = null;
    if (mode == "Edit") {
        //Render a form for updating the User

        //The data used to generate the Edit mode JSX
        let editItems = [
            {
                name: "Username",
                value: username,
                onChange: (ev) => {
                    setUsername(ev.target.value)
                }
            },
            {
                name: "Password",
                value: password,
                onChange: (ev) => {
                    setPassword(ev.target.value)
                }
            },
            {
                name: "First name",
                value: firstName,
                onChange: (ev) => {
                    setFirstName(ev.target.value)
                }
            },
            {
                name: "Last name",
                value: lastName,
                onChange: (ev) => {
                    setLastName(ev.target.value)
                }
            },
            {
                name: "Email",
                value: email,
                onChange: (ev) => {
                    setEmail(ev.target.value)
                }
            },
        ]

        //Map editItems to JSX
        let editItemsJSX = editItems.map(item => {
            return <div style={inputWrapperStyle} >
                <p style={{ marginRight: "5px" }} >{item.name}</p>
                <input style={inputStyle} value={item.value} onChange={item.onChange} />
            </div>
        })

        //Add some more JSX
        editItemsJSX.push(
            //Role selector drop down
            <ComboBox
                //Variables
                //items is a list of strings to display
                items={roles.map(role => role.role)}
                //Name is used in the html
                name={"Choose a Role"}
                //initIdx is the default selection in the list
                initIdx={roleIdx}

                //Functions
                //This is used to update the selectedIdx
                updateSelection={(idx) => {
                    setRoleIdx(idx)
                }}
            />,

            //Save button
            <button
                style={buttonStyle}
                onClick={() => { submit() }}>Save</button>
        )

        content = editItemsJSX
    } else if(mode == "View") {
        //Display the accounts data
        //The string displayed in View mode
        let items = [
            `Name: ${props.user.firstName} ${props.user.lastName}`,
            `Username: ${props.user.username}`,
            `Rmail: ${props.user.email}`,
            `Role: ${props.user.role.role}`
        ]

        //Map the strings to JSX
        let itemsJSX = items.map(item => {
            return <div style={{ margin: "10px 20px" }} >{item}</div>
        })

        content = itemsJSX
    }else if( mode == "Message" ){
        content = <h3 style={{color: messageColor}} >{message}</h3>
    }

    //The generated JSX
    return <DetailPanel
        name={"User"}
        color={"green"}
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


export { UserDetailPanel }