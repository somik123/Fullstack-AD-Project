import React, { useCallback, useContext, useMemo } from 'react'
import { Calendar, momentLocalizer, Views } from 'react-big-calendar'
import moment from 'moment'
import "react-big-calendar/lib/css/react-big-calendar.css";

import CreateEventDialog from './CreateEventDialog';
import ViewEventDialog from './ViewEventDialog';
import EventDataService from '../../api/event-actions';

import { EventContext } from '../../providers/eventProvider'
import { TabContext } from '../../providers/tabProvider'

const localizer = momentLocalizer(moment)

const MyCalendar = ({ dayLayoutAlgorithm = 'no-overlap',inputSnackBarDetails }) => {

    const { tabDispatch } = useContext(TabContext);
    const { eventContext, dispatch } = useContext(EventContext);
    const [createOpen, setCreateOpen] = React.useState(false);
    const [viewOpen, setViewOpen] = React.useState(false);
    const [eventDate, setEventDate] = React.useState({ start: moment().toDate(), end: moment().add(1, "days").toDate() });
    const { views } = useMemo(
        () => ({
            views: ['month', 'week', 'day', 'agenda'],
        }),
        []
    )

    const handleSelectSlot = useCallback(
        ({ start, end }) => {
            setCreateOpen(true);
            setEventDate({ start: moment(start).format(), end: moment(end).format() })
        },
        []
    )

    const handleClose = () => {
        setCreateOpen(false);
        setViewOpen(false);
    };

    const handleSelectEvent = useCallback(
        (event) => {
            EventDataService.getEventById(event.id)
                .then((response) => {
                    const item = response.data;
                    dispatch({
                        type: 'SET_EVENT',
                        event: item
                    });
                    setViewOpen(true);
                })
                .catch((e) => {
                    console.log(e.response);
                    alert("Something went wrong!");
                });
        },
        []
    )

    return (
        <div style={{ borderTop: '0.5px solid #1976d2' }}>
            {
                eventContext.eventList &&
                <>
                    <Calendar
                        dayLayoutAlgorithm={dayLayoutAlgorithm}
                        localizer={localizer}
                        defaultView={Views.MONTH}
                        events={eventContext.eventList}
                        onSelectEvent={handleSelectEvent}
                        onSelectSlot={handleSelectSlot}
                        selectable
                        views={views}
                        style={{ height: "100vh", marginTop: '10px' }}
                    />
                    <CreateEventDialog open={createOpen} onClose={() => handleClose()}
                        eventDate={eventDate} inputSnackBarDetails={inputSnackBarDetails} />
                    <ViewEventDialog open={viewOpen} onClose={() => handleClose()}
                        inputSnackBarDetails={inputSnackBarDetails} tabDispatch={tabDispatch} />
                </>
            }
        </div>
    );
}

export default MyCalendar;