import * as React from 'react';
import Dialog from '@mui/material/Dialog';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import CloseIcon from '@mui/icons-material/Close';
import Slide from '@mui/material/Slide';
import { Container } from '@mui/material';

const Transition = React.forwardRef(function Transition(props, ref) {
    return <Slide direction="up" ref={ref} {...props} />;
});

export default function FullScreenDialog(props) {

    const { open, handleClose, file, fileType } = props

    return (
        <div>
            <Dialog
                fullScreen
                open={open}
                onClose={handleClose}
                TransitionComponent={Transition}
            >
                <AppBar sx={{ position: 'relative' }}>
                    <Toolbar>
                        <IconButton
                            edge="start"
                            color="inherit"
                            onClick={handleClose}
                            aria-label="close"
                        >
                            <CloseIcon />
                        </IconButton>
                        <Typography sx={{ ml: 2, flex: 1 }} variant="h6" component="div">
                            Study Buddy
                        </Typography>
                    </Toolbar>
                </AppBar>

                <Container sx={{ height: '100%', padding: 5 }}>
                    {
                        fileType == "image" ?
                            <div
                                style={{
                                    backgroundImage: `url(${file})`,
                                    backgroundRepeat: 'no-repeat',
                                    height: '100%',
                                    backgroundSize: 'contain',
                                    backgroundPosition: 'center',
                                }} />
                            :
                            <video
                                style={{
                                    position: 'absolute',
                                    right: 0,
                                    bottom: 0,
                                    minWidth: '100%',
                                    minHeight: '94%',
                                    height:"94%"
                                }}
                                controls >
                                <source src={file} type="video/mp4" />
                                Your browser does not support the video tag.
                            </video>
                    }
                </Container>
            </Dialog>
        </div >
    );
}
