import React, { useEffect, useState } from 'react'
import {AccountSearchResult} from './AccountSearchResult'
import {UserSearchResult} from './UserSearchResult'
import {Tabs} from './Tabs'

function StandardSearchPanel(props) {
    //data loaded from server
    const [accounts, setAccounts] = useState([])

    //The mode
    const [mode, setMode] = useState("User")

    //The tabs
    let tabs = ['User', 'Accounts']

    useEffect(() => {
        //This runs once after the first render
        //Fetch all accounts
        fetch(`BankAPI/accounts/owner/${props.user.id}`, {
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
            setAccounts(json)
        }).catch(error => {
            //Just write error to console
            console.log(error)
        })
    }, [])

    //The list of accounts
    let accountsJSX = accounts.map((account) => {
        return <div onClick={() => props.setAccount(account)} >
            <AccountSearchResult account={account} />
        </div>
    })

    //The one user
    let userJSX = <div onClick={() => props.setUser(props.user)}>
            <UserSearchResult user={props.user} />
        </div>

    //The accounts or user to display
    let listContainer = null;
    if( mode == "User" ){
        listContainer = <div style={{ overflowY: "scroll", height: "450px" }} >{userJSX}</div>
    }else{
        listContainer = <div style={{ overflowY: "scroll", height: "450px" }} >{accountsJSX}</div>
    }

    //The returned JSX
    return [
        <Tabs items={tabs} updateSelected={(idx) => {
            setMode(tabs[idx])
        }} />,
        listContainer
    ]

    
}

export { StandardSearchPanel }
