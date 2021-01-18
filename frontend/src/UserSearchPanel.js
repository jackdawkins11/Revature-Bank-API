import React from 'react'
import { UserSearchResult } from './UserSearchResult'

function UserSearchPanel(props) {
    //JSX containing an <input> element for filtering Users
    let search = <div>
        <div style={{ fontSize: "13px", padding: "5px" }}>Search for users:</div>
        <input value={props.searchString} onChange={(ev) => {
            props.updateSearchResults(ev.target.value)
        }}
        style={{margin: "0px 5px", color: "inherit", backgroundColor: "gray", border: "none"}}
        />
    </div>
    //A list of Users
    let results = props.searchResults.map((user, idx) => {
        return <div onClick={() => props.updateSearchSelection(idx)}>
            <UserSearchResult user={user} />
        </div>
    })

    //Add container
    let resultsContainer = <div style={{overflowY: "scroll", height: "450px"}} >{results}</div>

    //Return JSX
    return <div>{[search, resultsContainer]}</div>
}

export { UserSearchPanel }