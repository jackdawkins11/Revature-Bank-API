import React from "react";
import { UserSearchPanel } from './UserSearchPanel'
import { AccountSearchPanel } from './AccountSearchPanel'
import { Tabs } from './Tabs'

class SearchPanel extends React.Component {

    constructor(props) {
        super(props)
        //Initialize state
        this.state = {
            //Determines the search mode
            searchMode: "Users",

            //Data loaded from server
            allUsers: [],
            allStatus: [],
            accountSearchResults: [],
            userSearchResults: []
        }

    }

    componentDidMount() {
        //Start the requests to load data from server
        //See each function for what data is loaded
        this.loadStatus()
        this.loadUsers()
        this.loadAllAccounts()
    }

    loadStatus() {
        //Fetch all AccountStatus
        fetch('BankAPI/accountStatus', {
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

    loadUsers() {
        //Fetch all Users
        fetch('BankAPI/users', {
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
            //Initialize userSearchResults to all users
            this.setState({
                allUsers: json,
                userSearchResults: json
            })
        }).catch(error => {
            //Just write error to console
            console.log(error)
        })
    }

    loadAllAccounts() {
        //Fetch all accounts
        fetch('BankAPI/accounts', {
            method: "GET"
        }).then(response => {
            //Check response status code
            if (!response.ok) {
                throw new Error("Could not fetch all accounts")
            }
            //Parse response json
            return response.json()
        }).then(json => {
            //Save the accounts
            this.setState({
                accountSearchResults: json
            })
        }).catch(error => {
            //Just write error to console
            console.log(error)
        })
    }

    //Changes the list of Accounts to display
    //according to the user's selection: selectedUser.
    updateAccountsByUser(selectedUser) {
        if (selectedUser < 1) {
            //This means the user selected the 'All' option
            this.loadAllAccounts()
            return
        }
        //Fetch accounts by user
        fetch(`BankAPI/accounts/owner/${selectedUser}`, {
            method: "GET"
        }).then(response => {
            //Check response status code
            if (!response.ok) {
                throw new Error("Could not fetch accounts by user")
            }
            //Parse response json
            return response.json()
        }).then(json => {
            //Update the search results
            this.setState({
                accountSearchResults: json
            })
        }).catch(error => {
            //Just write error to console
            console.log(error)
        })
    }

    //Changes accountSearchResults based on selectedStatus
    updateAccountsByStatus(selectedStatus) {
        if (selectedStatus < 1) {
            //This means the user selected the 'All' option
            this.loadAllAccounts()
            return
        }
        //Fetch accounts by status
        fetch(`BankAPI/accounts/status/${selectedStatus}`, {
            method: "GET"
        }).then(response => {
            //Check response status code
            if (!response.ok) {
                throw new Error("Could not fetch accounts by status")
            }
            //Parse response json
            return response.json()
        }).then(json => {
            //Update the search results
            this.setState({
                accountSearchResults: json
            })
        }).catch(error => {
            //Just write error to console
            console.log(error)
        })
    }

    updateUserSearchResults(search) {
        //We want case-insensitive
        search = search.toLowerCase()

        //The new state.userSearchResults
        let userSearchResults = []

        //All of the users in the system
        let allUsers = this.state.allUsers

        //Add all users with a first name, last name, username or email starting with 'search'
        for (let i = 0; i < allUsers.length; i++) {
            let username = allUsers[i].username.toLowerCase()
            let firstName = allUsers[i].firstName.toLowerCase()
            let lastName = allUsers[i].lastName.toLowerCase()
            let email = allUsers[i].email.toLowerCase()
            if (username.startsWith(search)
                || firstName.startsWith(search)
                || lastName.startsWith(search)
                || email.startsWith(search)) {
                userSearchResults.push(allUsers[i])
            }
        }

        //Set the new userSearchResults
        this.setState({
            userSearchResults: userSearchResults
        })
    }

    updateSelectedSearchResult(selectionIdx) {
        //Update the selected user or selected account
        if (this.state.searchMode == "Users" && 0 <= selectionIdx
            && selectionIdx < this.state.userSearchResults.length) {
            //Client selected a User from userSearchResults
            //Send it to the parent component
            this.props.setUser(this.state.userSearchResults[selectionIdx])
        
        } else if (this.state.searchMode == "Accounts" && 0 <= selectionIdx
            && selectionIdx < this.state.accountSearchResults.length) {
            //Client selected an Account from accountSearchResults
            //Send it to the parent component
            this.props.setAccount(this.state.accountSearchResults[selectionIdx])
        }
    }

    render() {
        //Generate the search panel
        let searchPanel = null
        if (this.state.searchMode == "Users") {
            searchPanel = <UserSearchPanel searchResults={this.state.userSearchResults}
                updateSearchResults={this.updateUserSearchResults.bind(this)}
                updateSearchSelection={this.updateSelectedSearchResult.bind(this)} />
        } else {
            searchPanel = <AccountSearchPanel allStatus={this.state.allStatus}
                allUsers={this.state.allUsers}
                searchResults={this.state.accountSearchResults}
                updateAccountsByStatus={this.updateAccountsByStatus.bind(this)}
                updateAccountsByUser={this.updateAccountsByUser.bind(this)}
                updateSearchSelection={this.updateSelectedSearchResult.bind(this)} />
        }

        let tabs = ["Users", "Accounts"]

        //The returned JSX
        return [
            <Tabs items={tabs} updateSelected={(idx) => {
                this.setState({ searchMode: tabs[idx] })
            }} />,
            searchPanel
        ]
    }
}

export { SearchPanel }