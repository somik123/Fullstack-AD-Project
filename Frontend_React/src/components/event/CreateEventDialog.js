import React, { useContext } from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import Stack from '@mui/material/Stack';
import Box from '@mui/material/Box';
import SaveIcon from '@mui/icons-material/Save';
import EventDataService from '../../api/event-actions';
import moment from 'moment'
import { EventContext } from '../../providers/eventProvider'
import { Claims } from '../../utils/ClientCache'

export default function CreateEventDialog(props) {

  const { eventContext, dispatch } = useContext(EventContext);
  const { open, onClose, eventDate,inputSnackBarDetails } = props

  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);

    let eventRequest = {
      name: data.get('title'),
      description: data.get('description'),
      username: Claims.getUserName(),
      createTime: moment(new Date()).utc(true).format(),
      groupName: data.get('groupName'),
      eventTime: moment(eventDate.start).utc(true).format()
    }

    if (eventRequest.name.length < 1) inputSnackBarDetails('Event name is required!','error');
    else if (eventRequest.description.length > 100) inputSnackBarDetails('Description is only allowed to put 100 characters!','error');
    else if (eventRequest.groupName.length < 1) inputSnackBarDetails('Group name is required!','error');
    else {
      EventDataService.createEvent(eventRequest)
        .then((response) => {
          const item = response.data;
          let eventList = eventContext.eventList;
          let start = moment(eventRequest.eventTime);
          let end = moment(eventRequest.eventTime);
          let title = eventRequest.name;
          eventList.push({ start: start._d, end, title, id: item.id })
          dispatch({
            type: 'SET_EVENT_LIST',
            eventList
          });
          inputSnackBarDetails("Congratulation! You just created new event..", 'success');
          onClose();
        })
        .catch((e) => {
          console.log(e.response);
          inputSnackBarDetails(e.response.data, 'error');
        });
    }
  }

  return (
    <div>
      <Dialog open={open} onClose={onClose} fullWidth>
        <DialogTitle>Create Event</DialogTitle>
        <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 1 }}>
          <DialogContent>

            <Stack spacing={2}>
              <TextField
                autoFocus
                margin="dense"
                id="title"
                name="title"
                placeholder="Event Title"
                type="text"
                fullWidth
                variant="outlined"

              />
              <TextField
                autoFocus
                margin="dense"
                id="description"
                name="description"
                placeholder='Description (Max 100 characters)'
                type="text"
                fullWidth
                multiline
                rows={4}
                variant="outlined"
              />
              <TextField
                autoFocus
                margin="dense"
                id="groupName"
                name="groupName"
                placeholder="Group Name"
                type="text"
                fullWidth
                variant="outlined"
              />
              <TextField
                disabled
                id="outlined-disabled"
                label="Event Date"
                variant="outlined"
                defaultValue={eventDate.start}
              />
            </Stack>
          </DialogContent>
          <DialogActions>
            <Button variant="contained" onClick={onClose}>Cancel</Button>
            <Button variant="contained" endIcon={<SaveIcon />} type="submit">Create</Button>
          </DialogActions>
        </Box>
      </Dialog>
    </div>
  );
}