import React from "react"
import { AccountSearchResult } from './AccountSearchResult'
import {ComboBox} from './ComboBox'

class AccountSearchPanel extends React.Component {

    constructor(props) {
        super(props)
    }

    render() {
        //The status drop down list
        let statusComboBox = <ComboBox
            //Variables
            //items is a list of strings to display
            items={this.props.allStatus.map(status => status.status)}
            //Name is used in the html
            name={"Find by Status"}
            //initIdx is the default selection in the list
            initIdx={-1}

            //Functions
            //This is used to update the selectedIdx
            updateSelection={(idx) => {
                if (idx >= 0) {
                    //If we have a valid index, call with a status id
                    this.props.updateAccountsByStatus(this.props.allStatus[idx].id)
                } else {
                    //If we have -1, call with -1
                    this.props.updateAccountsByStatus(-1)
                }
            }}
        />

        //The user drop down list
        let userComboBox = <ComboBox
            //Variables
            //items is a list of strings to display
            items={this.props.allUsers.map(user => user.username)}
            //Name is used in the html
            name={"Find by User"}
            //initIdx is the default selection in the list
            initIdx={-1}

            //Functions
            //This is used to update the selectedIdx
            updateSelection={(idx) => {
                if (idx >= 0) {
                    //If we have a valid index, call with a status id
                    this.props.updateAccountsByUser(this.props.allUsers[idx].id)
                } else {
                    //If we have -1, call with -1
                    this.props.updateAccountsByUser(-1)
                }
            }}
        />

        //The search results
        let resultPanel = this.props.searchResults.map((account, idx) => {
            return <div onClick={() => this.props.updateSearchSelection(idx)} >
                <AccountSearchResult account={account} />
            </div>
        })

        let resultsContainer = <div style={{overflowY: "scroll", height: "450px"}} >{resultPanel}</div>

        //The generated JSX
        return <div>
            <div style={{ display: "flex" }}>
                {[userComboBox, statusComboBox]}
            </div>
            {resultsContainer}
        </div>
    }
}

export { AccountSearchPanel }