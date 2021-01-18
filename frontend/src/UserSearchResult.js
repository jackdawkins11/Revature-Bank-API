import React from 'react'
import { SearchResult } from './SearchResult'

function UserSearchResult(props) {
    //Return a <SearchResult /> customized for the input user
    return <SearchResult name={"User"} color={"green"} items={[
        `Name: ${props.user.firstName} ${props.user.lastName}`,
        `Username: ${props.user.username}`,
        `Email: ${props.user.email}`
    ]} />
}

export { UserSearchResult }