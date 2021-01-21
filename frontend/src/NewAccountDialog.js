import React from 'react'
import { ComboBox } from './ComboBox'
import { buttonStyle, inputStyle, inputWrapperStyle } from './style'

class NewAccountDialog extends React.Component {

    constructor(props) {
        super(props)

        this.state = {
            //The user input
            amount: "",
            userIdx: -1,
            typeIdx: -1,
            statusIdx: -1,

            //The data loaded from the server
            allUsers: [],
            allStatus: [],
            allTypes: []
        }
    }

    componentDidMount(){
        //Start loading the data from the server
        this.loadStatus()
        this.loadTypes()

        //If client has Standard Role, only let them create personal accounts
        if( this.props.client.role.role == "Employee" || this.props.client.role.role == "Admin"){
            this.loadUsers()
        }else{
            this.setState({
                allUsers: [this.props.client]
            })
        }
    }

    loadStatus() {
        //Fetch all AccountStatus
        fetch('/BankAPI/accountStatus', {
            method: "GET"
        }).then(response => {
            //Check response status code
            if (!response.ok) {
                throw new Error("Could not fetch AccountStatus'")
            }
            //Parse response json
            return response.json()
        }).then(json => {
            //Save the AccountStatus'
            this.setState({
                allStatus: json
            })
        }).catch(error => {
            //Just write error to console
            console.log(error)
        })
    }

    loadTypes() {
        //Fetch all AccountTypes
        fetch('/BankAPI/accountTypes', {
            method: "GET"
        }).then(response => {
            //Check response status code
            if (!response.ok) {
                throw new Error("Could not fetch AccountTypes")
            }
            //Parse response json
            return response.json()
        }).then(json => {
            //Save the AccountTypes
            this.setState({
                allTypes: json
            })
        }).catch(error => {
            //Just write error to console
            console.log(error)
        })
    }

    loadUsers() {
        //Fetch all Users
        fetch('/BankAPI/users', {
            method: "GET"
        }).then(response => {
            //Check response status code
            if (!response.ok) {
                throw new Error("Could not fetch users")
            }
            //Parse response json
            return response.json()
        }).then(json => {
            //Save the Users
            this.setState({
                allUsers: json
            })
        }).catch(error => {
            //Just write error to console
            console.log(error)
        })
    }

    submit() {
        //Check user input
        if (this.state.userIdx == -1
            || this.state.typeIdx == -1
            || this.state.statusIdx == -1
            || !parseFloat(this.state.amount)) {
            this.props.error()
            return
        }

        //Get user input
        let userId = this.state.allUsers[this.state.userIdx].id
        let statusId = this.state.allStatus[this.state.statusIdx].id
        let typeId = this.state.allTypes[this.state.typeIdx].id
        let amount = parseFloat(this.state.amount)

        //Make request
        fetch('/BankAPI/accounts', {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                userId: userId,
                statusId: statusId,
                typeId: typeId,
                amount: amount
            })
        }).then(response => {
            if (!response.ok) {
                throw new Error("Request to create new account failed!")
            }
            return response.json()
        }).then(json => {
            this.props.success()
        }).catch(error => {
            this.props.error()
        })
    }

    render() {

        let userSelector = <ComboBox
            //Variables
            //items is a list of strings to display
            items={this.state.allUsers.map(user => user.username)}
            //Name is used in the html
            name={"Choose a User"}
            //initIdx is the default selection in the list
            initIdx={-1}

            //Functions
            //This is used to update the selectedIdx
            updateSelection={(idx) => {
                this.setState({
                    userIdx: idx
                })
            }}
        />

        let statusSelector = <ComboBox
            //Variables
            //items is a list of strings to display
            items={this.state.allStatus.map(status => status.status)}
            //Name is used in the html
            name={"Choose a Status"}
            //initIdx is the default selection in the list
            initIdx={-1}

            //Functions
            //This is used to update the selectedIdx
            updateSelection={(idx) => {
                this.setState({
                    statusIdx: idx
                })
            }}
        />

        let typeSelector = <ComboBox
            //Variables
            //items is a list of strings to display
            items={this.state.allTypes.map(type => type.type)}
            //Name is used in the html
            name={"Choose a Type"}
            //initIdx is the default selection in the list
            initIdx={-1}

            //Functions
            //This is used to update the selectedIdx
            updateSelection={(idx) => {
                this.setState({
                    typeIdx: idx
                })
            }}
        />

        return [<h2>Create a new account</h2>,
            <div style={{ display: "flex", flexDirection: "column", alignItems: "flex-start" }}>
                {[userSelector, statusSelector, typeSelector]}
                <div style={inputWrapperStyle} ><p>Amount: $</p><input onChange={(ev) => {
                    this.setState({ amount: ev.target.value })
                }}
                style={inputStyle} /></div>
                <button
                    style={buttonStyle}
                    onClick={this.submit.bind(this)}>Submit</button>
            </div>
        ]
    }
}

export { NewAccountDialog }