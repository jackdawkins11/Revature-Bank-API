import React from "react";
import { UserDetailPanel } from './UserDetailPanel'
import { AccountDetailPanel } from './AccountDetailPanel'
import { NullDetailPanel } from './NullDetailPanel'
import { TopPanel } from "./TopPanel";
import { NewDialog } from "./NewDialog";
import { SearchPanel } from "./SearchPanel";
import { StandardSearchPanel } from "./StandardSearchPanel";

class HomePage extends React.Component {

    constructor(props) {
        super(props)
        //Initialize state
        this.state = {
            //If detailMode == "Accounts", display account
            //If detailMode == "User", display user
            detailMode: "Accounts",
            user: null,
            account: null,
            //Whether to display a pop-up window for creating new users and accounts
            newDialog: false
        }
    }

    render() {
        //Generate the detail panel
        //Note: setting key=id is critical: this forces the DetailPanel to completely
        //refresh when the user changes
        let detailPanel = null
        if (this.state.detailMode == "Users" && this.state.user != null) {
            //In detailMode == 'Users' and there is a selected user
            detailPanel = <UserDetailPanel user={this.state.user} key={this.state.user.id}
                hasEditPermission={this.state.user.id == this.props.client.id || this.props.client.role.role == "Admin"} />

        } else if (this.state.detailMode == "Accounts" && this.state.account != null) {
            //In detail mode "Accounts" and there is a selected account
            detailPanel = <AccountDetailPanel account={this.state.account} key={this.state.account.id} 
                hasEditPermission={this.props.client.role.role == "Admin"} />

        } else {
            //None of the above. Display NullDetailPanel
            detailPanel = <NullDetailPanel />
        }

        //Optional NewAccountDialog
        let newDialog = null
        if (this.state.newDialog) {
            newDialog = <NewDialog close={() => {
                this.setState({
                    newDialog: false
                })
            }} 
            client={this.props.client}
            />
        } else {
            newDialog = <div></div>
        }

        //The search panel
        let searchPanel = null
        if (this.props.client.role.role == "Employee" || this.props.client.role.role == "Admin") {
            searchPanel = <SearchPanel
                setUser={(user) => {
                    this.setState({ user: user, detailMode: "Users" })
                }}
                setAccount={(account) => {
                    this.setState({ account: account, detailMode: "Accounts" })
                }}
            />
        } else {
            searchPanel = <StandardSearchPanel
                setUser={(user) => {
                    this.setState({ user: user, detailMode: "Users" })
                }}
                setAccount={(account) => {
                    this.setState({ account: account, detailMode: "Accounts" })
                }}
                user={this.props.client}
            />
        }

        //The returned JSX
        return <div>
            {newDialog}
            <TopPanel logout={this.props.tryLogout}
                startNewAccountDialog={() => {
                    this.setState({ newDialog: true })
                }}
                client={this.props.client}
            />
            <div style={{ display: "flex" }}>
                <div style={{ padding: "15px", width: "33%" }}>
                    {searchPanel}
                </div>
                <div style={{ padding: "15px" }}>
                    {detailPanel}
                </div>
            </div>
        </div>
    }
}

export { HomePage }