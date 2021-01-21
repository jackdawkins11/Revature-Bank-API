import React from 'react'
import { ComboBox } from './ComboBox'
import {inputWrapperStyle, inputStyle, buttonStyle} from './style'

class NewUserDialog extends React.Component {

    constructor(props) {
        super(props)

        this.state = {
            //The user input
            username: "",
            password: "",
            firstName: "",
            lastName: "",
            email: "",
            roleIdx: -1,

            //The data loaded from the server
            allRoles: []
        }
    }

    componentDidMount() {
        //Start loading the data from the server
        this.loadRoles()
    }

    loadRoles() {
        //Fetch all AccountStatus
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
            //Save the AccountStatus'
            this.setState({
                allRoles: json
            })
        }).catch(error => {
            //Just write error to console
            console.log(error)
        })
    }

    submit() {
        //Check user input
        if (this.state.roleIdx == -1
            || this.state.username == ""
            || this.state.password == ""
            || this.state.firstName == ""
            || this.state.lastName == ""
            || this.state.email == "") {
            this.props.error()
            return
        }

        //Get user input
        let roleId = this.state.allRoles[this.state.roleIdx].id

        //Make request
        fetch('/BankAPI/register', {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                username: this.state.username,
                password: this.state.password,
                firstName: this.state.firstName,
                lastName: this.state.lastName,
                email: this.state.email,
                roleId: roleId
            })
        }).then(response => {
            if (!response.ok) {
                throw new Error("Request to create new user failed!")
            }
            return response.json()
        }).then(json => {
            this.props.success()
        }).catch(error => {
            this.props.error()
        })
    }

    render() {

        let roleSelector = <ComboBox
            //Variables
            //items is a list of strings to display
            items={this.state.allRoles.map(role => role.role)}
            //Name is used in the html
            name={"Choose a Role"}
            //initIdx is the default selection in the list
            initIdx={-1}

            //Functions
            //This is used to update the selectedIdx
            updateSelection={(idx) => {
                this.setState({
                    roleIdx: idx
                })
            }}
        />

        //Used to generate user input JSX
        let inputData = [
            {
                name: "Username", update: (ev) => {
                    this.setState({ username: ev.target.value })
                }
            },
            {
                name: "Password", update: (ev) => {
                    this.setState({ password: ev.target.value })
                }
            },
            {
                name: "First name", update: (ev) => {
                    this.setState({ firstName: ev.target.value })
                }
            },
            {
                name: "Last name", update: (ev) => {
                    this.setState({ lastName: ev.target.value })
                }
            },
            {
                name: "Email", update: (ev) => {
                    this.setState({ email: ev.target.value })
                }
            },
        ]

        //A form for user to fill out
        let input = inputData.map(data => {
            return <div style={inputWrapperStyle} ><p>{data.name}: </p><input onChange={data.update}
                style={inputStyle}
            ></input></div>
        })

        //Return JSX
        return [<h2>Create a new user</h2>,
        <div style={{ display: "flex", flexDirection: "column", alignItems: "flex-start" }}>
            {[input, roleSelector]}
            <button
                style={buttonStyle}
                onClick={this.submit.bind(this)}>Submit</button>
        </div>
        ]
    }
}

export { NewUserDialog }