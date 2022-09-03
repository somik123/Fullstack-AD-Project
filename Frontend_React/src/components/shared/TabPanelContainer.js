import React, { useEffect, useContext } from "react";
import Tabs from '@mui/material/Tabs';
import { Tab, useMediaQuery } from '@mui/material';
import Icon from '@mui/material/Icon';
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import {
    useNavigate,
    Outlet
} from "react-router-dom";

import { TabContext } from '../../providers/tabProvider'

const TabPanelContainer = () => {
    const { tabContext, tabDispatch } = useContext(TabContext);
    let navigate = useNavigate();
    const largScreen = useMediaQuery(theme => theme.breakpoints.up('sm'));

    useEffect(() => {
        navigate("/")
        tabDispatch({ type: 'SET_INDEX', index: 0 });
    }, []);

    function renderRoute(param) {
        //onChange(param)
        tabDispatch({ type: 'SET_INDEX', index: param });
        switch (param) {
            case 0:
                return navigate("/");
            case 1:
                return navigate("/event");
            case 2:
                return navigate("/account");
            case 3:
                return navigate("/book");
            case 4:
                return navigate("/chat");
            // case 5:
            //     return navigate("/dashboard");
        }
    }

    return (
        <>
            <Tabs
                scrollButtons={true}
                allowScrollButtonsMobile
                variant={largScreen ? "fullWidth" : "scrollable"}
                value={tabContext.index}
                onChange={(e, val) => renderRoute(val)}>
                <Tab
                    className={"MuiTab--iconOnly"}
                    classes={{
                        wrapper: "MuiTab-wrapper"
                    }}
                    icon={
                        <Icon>dashboard</Icon>
                    }
                    disableRipple
                />
                <Tab
                    className={"MuiTab--iconOnly"}
                    classes={{
                        wrapper: "MuiTab-wrapper"
                    }}
                    icon={<CalendarMonthIcon />}
                    disableRipple
                />
                <Tab
                    className={"MuiTab--iconOnly"}
                    classes={{
                        wrapper: "MuiTab-wrapper"
                    }}
                    icon={
                        <Icon>person</Icon>
                    }
                    disableRipple
                />
                <Tab
                    className={"MuiTab--iconOnly"}
                    classes={{
                        wrapper: "MuiTab-wrapper"
                    }}
                    icon={<Icon>book</Icon>}
                    disableRipple
                />
                <Tab
                    className={"MuiTab--iconOnly"}
                    classes={{
                        wrapper: "MuiTab-wrapper"
                    }}
                    icon={<Icon>chat_outlined_rounded</Icon>}
                    disableRipple
                />
                {/* <Tab
                    className={"MuiTab--iconOnly"}
                    classes={{
                        wrapper: "MuiTab-wrapper"
                    }}
                    icon={
                        <Icon>dashboard</Icon>
                    }
                    disableRipple
                /> */}
            </Tabs>
            <div style={{ position: 'relative' }}>
                <Outlet />
            </div>

        </>
    );
};

TabPanelContainer.getTheme = muiBaseTheme => ({
    MuiTabs: {
        root: {
            width: "100%",
            borderBottom: "1px solid #E6ECF0"
        },
        indicator: {
            backgroundColor: "#1da1f2"
        }
    },
    MuiTab: {
        root: {
            minHeight: 53,
            minWidth: 0,
            [muiBaseTheme.breakpoints.up("md")]: {
                minWidth: 0
            },
            "&:hover": {
                "& .MuiTab-label": {
                    color: "#1da1f2"
                }
            },
            "&$selected": {
                "& *": {
                    color: "#1da1f2"
                }
            },
            "&.MuiTab--iconOnly": {
                "& .MuiTab-wrapper": {
                    width: "auto",
                    padding: 8,
                    borderRadius: 25,
                    color: "#657786",
                    "&:hover": {
                        color: "#1da1f2",
                        backgroundColor: "rgba(29, 161, 242, 0.1)"
                    }
                }
            }
        },
        textColorInherit: {
            opacity: 1
        },
        wrapper: {
            "& svg, .material-icons": {
                fontSize: 26.25
            }
        },
        labelContainer: {
            width: "100%",
            padding: 15,
            [muiBaseTheme.breakpoints.up("md")]: {
                padding: 15
            }
        },
        label: {
            textTransform: "none",
            fontSize: 15,
            fontWeight: 700,
            color: "#657786"
        }
    },
    MuiBadge: {
        root: {
            [`&.MuiBadge--dotted, &.MuiBadge--number`]: {
                "& .MuiBadge-badge": {
                    color: muiBaseTheme.palette.common.white,
                    backgroundColor: "#1da1f2",
                    minWidth: 0
                }
            },
            [`&.MuiBadge--dotted .MuiBadge-badge`]: {
                width: 6,
                height: 6,
                top: 0,
                right: 4,
                padding: 0
            },
            [`&.MuiBadge--number .MuiBadge-badge`]: {
                top: -4,
                right: 0,
                boxShadow: "rgb(255, 255, 255) 0px 0px 0px 0.14rem",
                width: 16,
                height: 16,
                fontSize: 10.7,
                fontWeight: "bold"
            }
        },
        colorPrimary: {
            color: muiBaseTheme.palette.common.white
        }
    }
});

export default TabPanelContainer;