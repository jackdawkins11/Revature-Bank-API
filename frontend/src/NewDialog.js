import React from 'react'
import { NewAccountDialog } from './NewAccountDialog'
import { Tabs } from './Tabs'
import { NewUserDialog } from './NewUserDialog'
import {buttonStyle} from './style'


class NewDialog extends React.Component {

    constructor(props) {
        super(props)

        this.state = {
            //Mode can be "Account", "User" or "Message"
            mode: "Account"
        }

        //Refs
        this.popUpRef = React.createRef()
    }

    //A click outside the generated popup box should close it 
    handleClickOutside(ev) {
        if (this.popUpRef && !this.popUpRef.current.contains(ev.target)) {
            this.props.close()
        }
    }

    componentDidMount() {
        document.addEventListener("mousedown", this.handleClickOutside.bind(this));

    }
    componentWillUnmount() {
        document.removeEventListener("mousedown", this.handleClickOutside.bind(this));
    }

    error() {
        this.setState({
            mode: "Error"
        })
    }

    success() {
        this.setState({
            mode: "Success"
        })
    }

    render() {

        let content = null;
        if ( this.props.client.role.role == "Admin" 
            && ( this.state.mode == "Account" || this.state.mode == "User" ) ) {
            //Admin and not in error or success mode
            let innerContent = null
            if (this.state.mode == "Account") {
                innerContent = <NewAccountDialog error={this.error.bind(this)}
                    success={this.success.bind(this)} client={this.props.client} />
            } else {
                innerContent = <NewUserDialog error={this.error.bind(this)}
                    success={this.success.bind(this)} />
            }
            let tabs = ["Account", "User"]
            content = [<Tabs items={tabs} updateSelected={(idx) => {
                this.setState({ mode: tabs[idx] })
            }} />, innerContent]

        }else if( this.state.mode == "Account" ){
            //Employee or Standard and not in "Error" or "Success" mode
            content = <NewAccountDialog error={this.error.bind(this)}
            success={this.success.bind(this)} client={this.props.client} />

        } else if (this.state.mode == "Error") {
            //Error mode: display message and button for returning to Account mode

            content = <div>
                <button
                    style={buttonStyle}
                     onClick={() => {
                        this.setState({
                            mode: "Account"
                        })
                    }} >Return</button>
                <h2 style={{ color: "red" }} >There was an error creating the resource</h2>
            </div>

        } else if (this.state.mode == "Success") {
            //Success mode is similar

            content = <div>
                <button
                    style={buttonStyle} onClick={this.props.close} >
                        OK</button>
                <h2 style={{ color: "green" }} >Resource created!</h2>
            </div>
        }

        //The generated JSX
        return <div style={{
            position: "fixed", left: 0, top: 0, zIndex: 101,
            width: "100%", height: "100%", display: "flex", justifyContent: "center",
            alignItems: "center"
        }}>
            <div ref={this.popUpRef}
                style={{ padding: "30px 60px", backgroundColor: "#696969", borderRadius: "20px" }}>
                {content}
            </div>
        </div>
    }
}

export { NewDialog }
