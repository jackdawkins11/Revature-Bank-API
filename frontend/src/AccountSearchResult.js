import React from 'react'
import { SearchResult } from './SearchResult'

/*
    Returns the SearchResult component customized for props.account
*/
function AccountSearchResult(props) {
    return <SearchResult name={"Account"} items={[
        `Status: ${props.account.status.status}`,
        `Type: ${props.account.type.type}`,
        `Balance: $${props.account.amount}`
    ]} color={"blue"}
    />
}

export { AccountSearchResult }