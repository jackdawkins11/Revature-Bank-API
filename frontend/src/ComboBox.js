import React from 'react'

let listItemStyle = {
    borderRadius: "5px",
    padding: "5px",
    backgroundColor: "#505050",
    width: "100px"
}

/*
    Reusable drop down Combo Box. Input:
        items: array of strings specifying the options
        updateSelection(index): function called when an element in items is selected
        name: text to display above the drop down list
        initIdx: the original index in the list to use
*/

class ComboBox extends React.Component {
    constructor(props) {
        super(props)

        //We store whether to display the drop down part
        //and the current selected item
        this.state = {
            focused: false,
            currentIdx: props.initIdx + 1
        }

        //Refs
        this.ref = React.createRef()
    }

    //Checks if the click event is outside of this element. If it
    //is outside, the drop down part is not displayed
    handleClickOutside(ev) {
        if (this.ref && !this.ref.current.contains(ev.target) ) {
            this.setState({focused: false})
        }
    }

    //Add the handleClickOutside event listener
    componentDidMount() {
        document.addEventListener("mousedown", this.handleClickOutside.bind(this));
    }

    componentWillUnmount() {
        document.removeEventListener("mousedown", this.handleClickOutside.bind(this));
    }

    render() {
        //Add the default item to the beginning of the list of items
        let selectionList = [...this.props.items]
        selectionList.unshift("All")

        //Map to a list of JSX elements
        let selectionListJSX = selectionList.map((str, idx) => {

            //Make the background of the selected element different
            let backgroundColor = null;
            if (idx == this.state.currentIdx) {
                backgroundColor = "grey"
            } else {
                backgroundColor = "inherit"
            }
            //On click, we update the selected index and call props.updateSelection
            //with index - 1, the index into the array passed to this node
            //as props, or -1
            return <div style={{ backgroundColor: backgroundColor }}
                onClick={() => {
                    this.setState({currentIdx: idx, focused: false})
                    this.props.updateSelection(idx - 1)
                }}>{str}</div>
        })

        //Don't display drop down items when not focused
        let selectionDisplay = "block"
        if (!this.state.focused) {
            selectionDisplay = "none"
        }

        return <div ref={this.ref} style={{ padding: "10px", cursor: "pointer" }} >
            <div style={{ fontSize: "12px" }}>{this.props.name}</div>
            <div >
                <div style={listItemStyle} onClick={() => {
                    this.setState({ focused: true })
                }}>
                    {selectionList[this.state.currentIdx]}
                </div>
                <div style={Object.assign({ display: selectionDisplay, position: "absolute", zIndex: 100 }, listItemStyle)}>{selectionListJSX}</div>
            </div>

        </div>
    }

}

export { ComboBox }