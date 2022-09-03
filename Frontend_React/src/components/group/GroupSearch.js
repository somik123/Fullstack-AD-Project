import React, { useContext } from 'react';
// @mui
import { styled } from '@mui/material/styles';
import { Autocomplete, InputAdornment, Popper, TextField } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
// ----------------------------------------------------------------------
import { GroupContext } from '../../providers/groupProvider'

const PopperStyle = styled((props) => <Popper placement="bottom-start" {...props} />)({
  width: '420px !important',
});


export default function GroupSearch() {

  const { groupContext, dispatch } = useContext(GroupContext);

  const handleSearch = (name) => {
    if (name.length > 0) {
      let filterGroups = groupContext.groupList.filter(t => t.name == name).slice(0, 1);
      console.log(filterGroups)
      dispatch({
        type: 'SET_FILTER_GROUP',
        filterGroupList: filterGroups
      });
    }
    else {
      dispatch({
        type: 'SET_FILTER_GROUP',
        filterGroupList: groupContext.groupList.slice(0, 10)
      });
    }
  };

  return (
    <Autocomplete
      onInputChange={(_event, newInputValue) => {
        handleSearch(newInputValue)
      }}
      sx={{ width: 420 }}
      autoHighlight
      popupIcon={null}
      PopperComponent={PopperStyle}
      options={groupContext.groupList}
      getOptionLabel={(group) => group.name}
      isOptionEqualToValue={(option, value) => option.id === value.id}
      renderInput={(params) => (
        <TextField
          {...params}
          placeholder="Search group..."
          InputProps={{
            ...params.InputProps,
            startAdornment: (
              <InputAdornment position="start">
                <SearchIcon sx={{ ml: 1, width: 20, height: 20, color: 'text.disabled' }} />
              </InputAdornment>
            ),
          }}
        />
      )}
    />
  );
}