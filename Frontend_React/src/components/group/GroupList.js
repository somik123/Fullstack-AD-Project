import * as React from 'react';
// material
import { Grid } from '@mui/material';
// components
import GroupCard from './GroupCard';
import Pagination from '@mui/material/Pagination';
import Stack from '@mui/material/Stack';

export default function GroupList({ groups, tabDispatch }) {

  const [page, setPage] = React.useState(1);
  const handleChange = (_event, value) => {
    setPage(value);
  };
  let count = Math.ceil(groups.length / 4);

  return (
    <Stack spacing={2}>
      <Grid container spacing={2}>
        {groups.slice((page - 1) * 4, page * 4).map((group) => (
          <GroupCard key={group.id} group={group} tabDispatch={tabDispatch} />
        ))}
      </Grid>
      <div style={{ display: 'flex', justifyContent: 'center', marginTop: 15 }}>
        <Pagination count={count} page={page} onChange={handleChange} />
      </div>
    </Stack>
  );
}