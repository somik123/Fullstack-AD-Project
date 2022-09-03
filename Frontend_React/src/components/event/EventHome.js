import React, { useReducer, useEffect } from 'react'
import moment from 'moment'
import "react-big-calendar/lib/css/react-big-calendar.css";
import MyCalendar from './MyCalendar';
import Loading from './../shared/Loading';
import { Container } from '@mui/material';
import {
    Snackbar,
    Alert
} from '@mui/material';

import EventDataService from '../../api/event-actions';
import { eventReducer, initialState } from '../../reducers/eventReducer'
import { EventProvider } from '../../providers/eventProvider'


const EventHome = () => {

    document.body.style.overflowY = 'auto';
    const [eventContext, dispatch] = useReducer(eventReducer, initialState);

    useEffect(() => {
        const initEvents = () => {
            dispatch({ type: 'SET_LOADING', loading: true });
            EventDataService.getAllEvents()
                .then((response) => {
                    const items = response.data;
                    let eventList = loadEvents(items);
                    dispatch({
                        type: 'SET_EVENT_LIST',
                        eventList
                    });

                    dispatch({ type: 'SET_LOADING', loading: false });
                })
                .catch((e) => {
                    console.log(e.response);
                    if (e.response.status == 401) alert("Something went wrong!");
                });

        };
        initEvents();
    }, []);

    const loadEvents = (items) => {
        let eventList = []
        items.map((data) => {
            let start = moment(data.eventTime);
            let end = moment(data.eventTime).add(1, 'hours');
            let title = data.name;
            eventList.push({ start: start._d, end: end._d, title, id: data.id })
        })
        return eventList;
        //console.log(myEvents)
    };

    const handleSnackBarClose = () => {
        eventContext.snackBarDetails.open = false;
        dispatch({ type: 'SET_SNACKBARDETAILS', snackBarDetails: eventContext.snackBarDetails });
    };

    const inputSnackBarDetails = (message, type) => {
        eventContext.snackBarDetails = {
            open: true,
            message,
            type
        };
        dispatch({ type: 'SET_SNACKBARDETAILS', snackBarDetails: eventContext.snackBarDetails });
    }

    if (eventContext.loading) return <Container><Loading /></Container>;

    return (
        <div style={{ borderTop: '0.5px solid #1976d2' }}>
            <EventProvider value={{ eventContext, dispatch }}>
                <MyCalendar inputSnackBarDetails={inputSnackBarDetails}/>
                <Snackbar
                    anchorOrigin={{ horizontal: 'center', vertical: 'bottom' }}
                    key={`bottom, center`}
                    open={eventContext.snackBarDetails.open}
                    autoHideDuration={6000}
                    onClose={handleSnackBarClose}>
                    <Alert onClose={handleSnackBarClose} severity={eventContext.snackBarDetails.type}>
                        {eventContext.snackBarDetails.message}
                    </Alert>
                </Snackbar>
            </EventProvider>
        </div>
    );
}

export default EventHome;