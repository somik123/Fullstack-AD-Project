import React, { useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Typography from '@mui/material/Typography';
import { Chip, Stack, Tooltip } from '@mui/material';
import PersonAddAltIcon from '@mui/icons-material/PersonAddAlt';
import EventIcon from '@mui/icons-material/Event';
import Box from '@mui/material/Box';
import Avatar from '@mui/material/Avatar';
import AvatarGroup from '@mui/material/AvatarGroup';
import ChatIcon from '@mui/icons-material/Chat';

import { Claims } from '../../utils/ClientCache'
import EventDataService from '../../api/event-actions';
import { EventContext } from '../../providers/eventProvider'

export default function ViewEventDialog(props) {

    const { eventContext } = useContext(EventContext);
    const { open, onClose, inputSnackBarDetails, tabDispatch } = props
    let navigate = useNavigate();

    const handleSubmit = (value) => {
        if (value == "view") {
            //go to view group
            tabDispatch({ type: 'SET_INDEX', index: 4 });
            navigate(`/chat/${eventContext.event.groupId}`)
        }
        else {
            //join event
            EventDataService.joinEvent(eventContext.event.id, Claims.getUserId())
                .then((_response) => {
                    inputSnackBarDetails("Congratulation! You just joined to the event..", 'success');
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
            {
                eventContext.event &&
                <Dialog open={open} onClose={onClose} fullWidth>
                    <DialogTitle>Event Details</DialogTitle>
                    <Box>
                        <DialogContent>
                            <Card sx={{ width: '100%' }}>
                                <div style={{ position: 'relative', textAlign: 'center', color: 'white' }}>
                                    <CardMedia
                                        component="img"
                                        height="140"
                                        image="/assets/covers/event_banner.jpg"
                                        alt="green iguana"
                                    />
                                    <Typography style={{ position: 'absolute', top: '50%', left: '50%', transform: 'translate(-50%, -50%)' }} variant="h4" component="div">
                                        {eventContext.event.name}
                                    </Typography>
                                </div>

                                <CardContent>
                                    <Typography gutterBottom variant="h5" component="div">
                                        {eventContext.event.groupName}
                                    </Typography>
                                    <Stack direction="row" spacing={2}>
                                        <Chip sx={{ color: 'brown' }}
                                            avatar={<Avatar alt={eventContext.event.userName}
                                                src={eventContext.event.participantPhoto[eventContext.event.participantName.indexOf(eventContext.event.userName)]} />}
                                            label={`Created by ${eventContext.event.userName}`}
                                            variant="outlined"
                                        />
                                        <Chip sx={{ color: 'brown' }}
                                            icon={<EventIcon />}
                                            label={eventContext.event.eventTime}
                                            variant="outlined"
                                        />
                                    </Stack>
                                    <Stack sx={{ mt: 3 }} direction="column" spacing={2}>
                                        <Typography variant="body2" color="text.secondary">
                                            {eventContext.event.description}
                                        </Typography>
                                    </Stack>
                                </CardContent>
                                <CardActions>
                                    <AvatarGroup max={4}>
                                        {eventContext.event.participantPhoto.map((txt, index) =>
                                            <Tooltip key={index} title={eventContext.event.participantName[index]}>
                                                <Avatar alt="Remy Sharp" src={txt} />
                                            </Tooltip>
                                        )}
                                    </AvatarGroup>
                                </CardActions>
                            </Card>

                        </DialogContent>
                        <DialogActions>
                            <Button variant="contained" onClick={onClose}>Cancel</Button>
                            {
                                eventContext.event.userName == Claims.getUserName()
                                    || eventContext.event.participantName.indexOf(Claims.getUserName()) > -1 ?
                                    <Button variant="contained" endIcon={<ChatIcon />}
                                        onClick={() => handleSubmit('view')}>View Discussion</Button>
                                    :
                                    <Button variant="contained" endIcon={<PersonAddAltIcon />}
                                        onClick={() => handleSubmit('join')}>Join Event</Button>
                            }

                        </DialogActions>
                    </Box>
                </Dialog>
            }
        </div >
    );
}